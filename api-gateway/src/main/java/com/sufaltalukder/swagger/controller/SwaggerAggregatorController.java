package com.sufaltalukder.swagger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SwaggerAggregatorController {

	@Autowired
	private DiscoveryClient discoveryClient;

	private final WebClient webClient = WebClient.create();

	@GetMapping("/swagger-resources")
	public ResponseEntity<Map<String, String>> getSwaggerResources() {
		Map<String, String> swaggerUrls = new HashMap<>();

		discoveryClient.getServices().forEach(service -> {
			String url = "http://" + service.toLowerCase() + "/v3/api-docs";
			swaggerUrls.put(service + "-service", url);
		});

		return ResponseEntity.ok(swaggerUrls);
	}
}
