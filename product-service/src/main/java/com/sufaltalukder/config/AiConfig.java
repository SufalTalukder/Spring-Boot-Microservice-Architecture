package com.sufaltalukder.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        // SimpleVectorStore মেমোরিতে ডাটা সেভ রাখে (বিগিনারদের জন্য বেস্ট)
        return new SimpleVectorStore(embeddingModel);
    }
}
