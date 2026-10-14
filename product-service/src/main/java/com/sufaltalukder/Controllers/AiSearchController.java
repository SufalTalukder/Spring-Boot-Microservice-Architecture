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

    // Load all products from the database to AI (Vector Store)
    @GetMapping("/load-all")
    public String loadAllProductsToAi() {
        List<ProductModel> allProducts = productRepository.findAll();

        for (ProductModel product : allProducts) {
            productAiService.loadProductToAi(product);
        }

        return "Successfully loaded " + allProducts.size() + " products into AI Memory!";
    }

    // Semantic Search API
    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String query) {

        List<Document> documents = productAiService.searchProducts(query);
        List<Map<String, Object>> response = new ArrayList<>();

        // If no products are found in the database or don't match
        if (documents.isEmpty()) {
            Map<String, Object> notFoundMessage = new HashMap<>();
            notFoundMessage.put("message", "Sorry, no matching products were found in our store!");
            response.add(notFoundMessage);
            return response;
        }

        for (Document doc : documents) {
            Map<String, Object> productData = new HashMap<>();
            // Extracting the main description and product ID from the document
            productData.put("description", doc.getContent());
            productData.put("metadata", doc.getMetadata());
            response.add(productData);
        }

        return response;
    }

    // RAG Chat API
    @GetMapping("/chat")
    public Map<String, String> chatSearch(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "session-user-1") String sessionId) {
        String answer = productAiService.smartChatSearch(query, sessionId);
        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return response;
    }
}
