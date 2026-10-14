package com.sufaltalukder.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    // Vector Store Configuration (For Storing Product Embeddings)
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }

    // Memory Configuration (For Chat History)
    @Bean
    public org.springframework.ai.chat.memory.ChatMemory chatMemory() {
        return new org.springframework.ai.chat.memory.InMemoryChatMemory();
    }
}
