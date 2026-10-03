package com.sufaltalukder.Controllers;

import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Services.ProductAiService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product/ai")
public class AiSearchController {

    @Autowired
    private ProductAiService productAiService;

    @Autowired
    private ProductRepository productRepository;

    // ১. ডাটাবেসের সব প্রোডাক্ট এক ক্লিকে AI-কে শেখানোর (Vector Store এ লোড করার) API
    @GetMapping("/load-all")
    public String loadAllProductsToAi() {
        List<ProductModel> allProducts = productRepository.findAll();
        
        for (ProductModel product : allProducts) {
            productAiService.loadProductToAi(product);
        }
        
        return "Successfully loaded " + allProducts.size() + " products into AI Memory!";
    }

    // ২. স্মার্ট সার্চ API
    // Example: http://localhost:8086/api/v1/product/ai/search?query=comfortable shoes for winter
    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String query) {
        
        List<Document> documents = productAiService.searchProducts(query);
        List<Map<String, Object>> response = new ArrayList<>();
        
        // যদি ডাটাবেসে প্রোডাক্ট না থাকে বা ম্যাচ না করে
        if (documents.isEmpty()) {
            Map<String, Object> notFoundMessage = new HashMap<>();
            notFoundMessage.put("message", "Sorry, no matching products were found in our store!");
            response.add(notFoundMessage);
            return response;
        }
        
        for (Document doc : documents) {
            Map<String, Object> productData = new HashMap<>();
            // Document থেকে মূল বিবরণ এবং প্রোডাক্ট আইডি বের করে আনা হচ্ছে
            productData.put("description", doc.getContent());
            productData.put("metadata", doc.getMetadata());
            response.add(productData);
        }
        
        return response;
    }

    // ৩. চ্যাট API (RAG - মানুষের মতো গুছিয়ে উত্তর দেওয়ার জন্য)
    // Example: http://localhost:8086/api/v1/product/ai/chat?query=apple er kon phone valo
    @GetMapping("/chat")
    public Map<String, String> chatSearch(@RequestParam String query) {
        String answer = productAiService.smartChatSearch(query);
        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return response;
    }
}
