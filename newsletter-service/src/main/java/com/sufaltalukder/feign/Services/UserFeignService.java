package com.sufaltalukder.feign.Services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.ApiResponse;

@FeignClient("USER-SERVICE")
public interface UserFeignService {

	@GetMapping("/api/v1/elastic/user/is-newsletter-subscribed")
	ApiResponse<NewsletterDTO> getNewsletterSubscribed(@RequestParam long userId);

}
