package com.sufaltalukder.Services;

import com.sufaltalukder.Models.ProductModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductAiService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private org.springframework.ai.chat.model.ChatModel chatModel;

    // ১. প্রোডাক্টগুলো AI-কে পড়ানো (Convert to Vector and Save)
    public void loadProductToAi(ProductModel product) {
        // AI-এর বোঝার সুবিধার্থে প্রোডাক্টের একটি শক্তিশালী ও বিস্তারিত বিবরণ তৈরি করা
        String categoryName = product.getCategoryInfo() != null ? product.getCategoryInfo().getCategoryName() : "N/A";
        String subCategoryName = product.getSubCategoryInfo() != null ? product.getSubCategoryInfo().getSubCategoryName() : "N/A";
        String stockStatus = product.getProductStock() != null ? product.getProductStock().name() : "UNKNOWN";

        String productInfo = String.format(
            "Product Name: %s\nBrand: %s\nCategory: %s\nSub-Category: %s\nPrice: %.2f BDT\nStock Status: %s\nDescription: %s",
            product.getProductName(),
            product.getProductBrand() != null ? product.getProductBrand() : "No Brand",
            categoryName,
            subCategoryName,
            product.getProductPrice(),
            stockStatus,
            product.getProductDetails() != null ? product.getProductDetails() : "No description available"
        );
                             
        // Spring AI এর Document তৈরি করা (সাথে প্রোডাক্টের মেটাডাটা রেখে দিচ্ছি চেনার জন্য)
        Document document = new Document(productInfo, Map.of(
            "productId", product.getProductId(),
            "productName", product.getProductName(),
            "price", product.getProductPrice(),
            "category", categoryName
        ));
        
        // ভেক্টর স্টোরে সেভ করা
        vectorStore.add(List.of(document));
        System.out.println("Product added to AI Vector Store: " + product.getProductName());
    }

    // ২. স্মার্ট সার্চ করা (Semantic Search)
    public List<Document> searchProducts(String userQuery) {
        return vectorStore.similaritySearch(userQuery);
    }

    // ৩. চ্যাট মডেল দিয়ে RAG (Retrieval-Augmented Generation) সার্চ
    public String smartChatSearch(String userQuery) {
        // ১. সাধারণ SearchRequest তৈরি
        org.springframework.ai.vectorstore.SearchRequest searchRequest = org.springframework.ai.vectorstore.SearchRequest.query(userQuery);

        // ২. E-commerce Advanced Price Filtering Logic
        Double minPrice = null;
        Double maxPrice = null;

        // চেক ১: Range (e.g., between 500 and 1000)
        java.util.regex.Pattern rangePattern = java.util.regex.Pattern.compile("(?:between|from)\\s+(\\d+)\\s+(?:to|and|-)\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher rangeMatcher = rangePattern.matcher(userQuery);
        
        if (rangeMatcher.find()) {
            minPrice = Double.parseDouble(rangeMatcher.group(1));
            maxPrice = Double.parseDouble(rangeMatcher.group(2));
        } else {
            // চেক ২: Max Price (e.g., below 500, under 1000, budget 2000, kom 5000)
            java.util.regex.Pattern maxPattern = java.util.regex.Pattern.compile("(?:below|under|kom|max|maximum|budget|within)\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher maxMatcher = maxPattern.matcher(userQuery);
            if (maxMatcher.find()) {
                maxPrice = Double.parseDouble(maxMatcher.group(1));
            }

            // চেক ৩: Min Price (e.g., above 500, over 1000, beshi 2000, min 5000)
            java.util.regex.Pattern minPattern = java.util.regex.Pattern.compile("(?:above|over|beshi|min|minimum)\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher minMatcher = minPattern.matcher(userQuery);
            if (minMatcher.find()) {
                minPrice = Double.parseDouble(minMatcher.group(1));
            }
        }

        // ৩. Vector Store থেকে বেশি পরিমাণ প্রোডাক্ট বের করা (যাতে ফিল্টার করার পর কিছু বাকি থাকে)
        searchRequest = searchRequest.withTopK(30); 
        List<Document> similarProducts = vectorStore.similaritySearch(searchRequest);

        // ৪. ম্যানুয়ালি Metadata Filtering (যেহেতু SimpleVectorStore বিল্ট-ইন ফিল্টারিং সাপোর্ট করে না)
        if (minPrice != null || maxPrice != null) {
            final double finalMin = minPrice != null ? minPrice : 0.0;
            final double finalMax = maxPrice != null ? maxPrice : Double.MAX_VALUE;
            
            similarProducts = similarProducts.stream().filter(doc -> {
                if (doc.getMetadata().containsKey("price")) {
                    try {
                        double price = Double.parseDouble(doc.getMetadata().get("price").toString());
                        return price >= finalMin && price <= finalMax;
                    } catch (Exception e) {
                        return false;
                    }
                }
                return false;
            }).collect(java.util.stream.Collectors.toList());
            
            System.out.println("Applied Manual Java Filter. Products remaining: " + similarProducts.size());
        }
        
        // ২. যদি কোনো প্রোডাক্ট না পাওয়া যায়
        if (similarProducts.isEmpty()) {
            return "Sorry, no matching products were found in our store for your query!";
        }
        
        // ৩. প্রোডাক্টগুলোর ইনফরমেশন একত্র করা
        String productData = similarProducts.stream()
                .map(Document::getContent)
                .collect(java.util.stream.Collectors.joining("\n"));
                
        // ৪. উন্নত Prompt Engineering (AI-কে স্মার্ট ইনস্ট্রাকশন দেওয়া)
        String prompt = "You are a smart, professional e-commerce AI assistant. "
                + "Follow these rules STRICTLY:\n"
                + "1. ALWAYS reply in the exact same language as the User Question. Never translate.\n"
                + "2. Base your answer ONLY on the 'Available Products' list below. NEVER invent, guess, or make up products. If a product is not in the list, it does NOT exist in our store.\n"
                + "3. Respect numeric constraints like price/budget strictly. If they want items below 1000, do NOT show items above 1000.\n"
                + "4. For generic searches, be flexible. If you don't have the exact word but have related items in the list, recommend them enthusiastically!\n"
                + "5. If you cannot find any matching or related products in the 'Available Products' list, politely say you don't have it and DO NOT offer any imaginary alternatives.\n\n"
                + "--- Available Products ---\n" 
                + productData + "\n\n"
                + "--- User Question ---\n" 
                + userQuery;
                
        // ৫. Chat Model কে কল করা এবং গুছানো উত্তর নেওয়া
        return chatModel.call(prompt);
    }
}
