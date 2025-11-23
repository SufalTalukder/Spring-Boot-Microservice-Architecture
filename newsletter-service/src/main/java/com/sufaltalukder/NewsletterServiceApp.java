package com.sufaltalukder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.sufaltalukder.feign.Services")
public class NewsletterServiceApp {
	public static void main(String[] args) {
		SpringApplication.run(NewsletterServiceApp.class, args);
	}
}