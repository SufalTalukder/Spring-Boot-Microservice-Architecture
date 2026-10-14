package com.sufaltalukder.Services;

import com.sufaltalukder.Models.ProductModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductAiService {

    private final VectorStore vectorStore;
    private final org.springframework.ai.chat.client.ChatClient chatClient;

    // Constructor Injection (For Dependency Injection)
    public ProductAiService(VectorStore vectorStore,
            org.springframework.ai.chat.client.ChatClient.Builder chatClientBuilder,
            org.springframework.ai.chat.memory.ChatMemory chatMemory) {
        this.vectorStore = vectorStore;
        // Limit the chat history window to 3 messages to prevent Ollama "Read timed out" due to huge context size
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor(chatMemory, "default", 3))
                .build();
    }

    // 1. Convert to Vector and Save
    public void loadProductToAi(ProductModel product) {
        // Create a strong and detailed description of the product for the AI to
        // understand easily
        String categoryName = product.getCategoryInfo() != null ? product.getCategoryInfo().getCategoryName() : "N/A";
        String subCategoryName = product.getSubCategoryInfo() != null
                ? product.getSubCategoryInfo().getSubCategoryName()
                : "N/A";
        String stockStatus = product.getProductStock() != null ? product.getProductStock().name() : "UNKNOWN";

        String productInfo = String.format(
                "Product Name: %s\nBrand: %s\nCategory: %s\nSub-Category: %s\nPrice: %.2f BDT\nStock Status: %s\nDescription: %s",
                product.getProductName(),
                product.getProductBrand() != null ? product.getProductBrand() : "No Brand",
                categoryName,
                subCategoryName,
                product.getProductPrice(),
                stockStatus,
                product.getProductDetails() != null ? product.getProductDetails() : "No description available");

        // Create Spring AI Document (keeping product metadata for identification)
        Document document = new Document(productInfo, Map.of(
                "productId", product.getProductId(),
                "productName", product.getProductName(),
                "price", product.getProductPrice(),
                "category", categoryName));

        // Save to Vector Store
        vectorStore.add(List.of(document));
        System.out.println("Product added to AI Vector Store: " + product.getProductName());
    }

    // 2. Semantic Search
    public List<Document> searchProducts(String userQuery) {
        return vectorStore.similaritySearch(userQuery);
    }

    // 3. RAG (Retrieval-Augmented Generation) Search using Chat Model
    public String smartChatSearch(String userQuery, String sessionId) {
        // 1. Create a simple SearchRequest
        org.springframework.ai.vectorstore.SearchRequest searchRequest = org.springframework.ai.vectorstore.SearchRequest
                .query(userQuery);

        // 2. E-commerce Advanced Price Filtering Logic (Supports English & Banglish)
        Double minPrice = null;
        Double maxPrice = null;

        // Check 1: Range (e.g., between 500 and 1000, 500 to 1000, 500 theke 1000)
        java.util.regex.Pattern rangePattern = java.util.regex.Pattern.compile(
                "(?:(?:between|from)\\s+(\\d+)\\s+(?:to|and|-)\\s+(\\d+)|(\\d+)\\s*(?:theke|to|-)\\s*(\\d+))",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher rangeMatcher = rangePattern.matcher(userQuery);

        if (rangeMatcher.find()) {
            if (rangeMatcher.group(1) != null) {
                minPrice = Double.parseDouble(rangeMatcher.group(1));
                maxPrice = Double.parseDouble(rangeMatcher.group(2));
            } else {
                minPrice = Double.parseDouble(rangeMatcher.group(3));
                maxPrice = Double.parseDouble(rangeMatcher.group(4));
            }
        } else {
            // Check 2: Min Price / Above (e.g., "1000 er opore", "1000 er beshi", "1000 theke beshi", "above 1000", "over 1000", "min 1000")
            java.util.regex.Pattern minPattern = java.util.regex.Pattern.compile(
                    "(?:(\\d+)\\s*(?:er\\s+|theke\\s+)?(?:opore|upore|beshi|er\\s+beshi)|(?:above|over|more than|min|minimum)\\s+(\\d+))",
                    java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher minMatcher = minPattern.matcher(userQuery);
            if (minMatcher.find()) {
                minPrice = Double.parseDouble(minMatcher.group(1) != null ? minMatcher.group(1) : minMatcher.group(2));
            }

            // Check 3: Max Price / Below (e.g., "1000 er niche", "1000 er kom", "1000 theke kom", "below 1000", "under 1000", "budget 1000")
            java.util.regex.Pattern maxPattern = java.util.regex.Pattern.compile(
                    "(?:(\\d+)\\s*(?:er\\s+|theke\\s+)?(?:niche|kom|er\\s+kom|budget|er\\s+moddhe)|(?:below|under|budget|max|maximum|within|less than)\\s+(\\d+))",
                    java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher maxMatcher = maxPattern.matcher(userQuery);
            if (maxMatcher.find()) {
                maxPrice = Double.parseDouble(maxMatcher.group(1) != null ? maxMatcher.group(1) : maxMatcher.group(2));
            }
        }

        // 3. Get more products from Vector Store
        searchRequest = searchRequest.withTopK(30);
        List<Document> similarProducts = vectorStore.similaritySearch(searchRequest);

        // 4. Manual Metadata Filtering (since SimpleVectorStore does not support built-in filtering)
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

            System.out.println("Applied E-commerce Filter: min=" + minPrice + ", max=" + maxPrice + ". Remaining: " + similarProducts.size());
        }

        // 5. Limit the number of products passed to the LLM to avoid Timeout/CPU overload
        if (similarProducts.size() > 3) {
            similarProducts = similarProducts.subList(0, 3);
        }

        // 2. If no products are found
        if (similarProducts.isEmpty()) {
            return "Sorry, no matching products were found in our store for your query!";
        }

        // 3. Combine product information
        String productData = similarProducts.stream()
                .map(Document::getContent)
                .collect(java.util.stream.Collectors.joining("\n"));

        // 4. System Prompt Engineering (giving smart instructions to AI)
        String systemPrompt = "You are a friendly E-Commerce AI Shopping Assistant for this online store. Our store sells: Electronics, Tech Gadgets, Fashion, Shoes, and Clothes.\n\n"
                + "RULE 1 - ALWAYS ANSWER SHOPPING QUERIES (HIGHEST PRIORITY):\n"
                + "If the user asks about ANY product, brand, category, price, availability, recommendation, comparison, or anything related to shopping — YOU MUST ANSWER HELPFULLY using the 'Available Products' list below. Users may write in English, Bengali, or Banglish (e.g., 'sneakers aa6e?' means 'do you have sneakers?', 'dam koto?' means 'what is the price?', 'best headphone kon ta?' means 'which is the best headphone?'). Understand and respond to ALL such queries.\n\n"
                + "RULE 2 - REFUSE ONLY CLEARLY NON-SHOPPING TOPICS:\n"
                + "ONLY refuse if the user clearly asks about: coding/programming, finance/investments/mutual funds/stocks, math, science, medical advice, legal advice, politics, or general knowledge trivia. In such cases, politely say: 'I am only a shopping assistant for this store 🛍️. I can only help you find and explore products in our catalog! What product are you looking for? 😊'\n\n"
                + "RULE 3 - ONLY USE 'AVAILABLE PRODUCTS':\n"
                + "Recommend or describe ONLY products from the 'Available Products' list below. NEVER invent or hallucinate products. If no matching product exists, say: 'Sorry, that item is not currently available in our store 🛍️'\n\n"
                + "RULE 4 - PRICE & BUDGET: Respect price constraints (above/below/within budget). Show prices in BDT.\n\n"
                + "RULE 5 - LANGUAGE: Reply in the same language style as the user. Keep brand names and product type names in English as-is (e.g., 'Nike', 'Sneakers', 'T-Shirt').\n\n"
                + "RULE 6 - TONE & EMOJIS: Be friendly and natural. Use emojis like 👋🛍️🛒💰⭐✨📦💡 where appropriate.\n\n"
                + "--- Available Products ---\n"
                + productData;

        // 5. Call Chat Client with Contextual Memory and get the answer
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userQuery)
                .advisors(a -> a.param(
                        org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                        sessionId != null ? sessionId : "session-user-1"))
                .call()
                .content();
    }

    public String smartChatSearch(String userQuery) {
        return smartChatSearch(userQuery, "session-user-1");
    }
}
