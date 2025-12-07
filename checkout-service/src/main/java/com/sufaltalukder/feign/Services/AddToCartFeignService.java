package com.sufaltalukder.feign.Services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;

@FeignClient(name = "ADD-TO-CART-SERVICE")
public interface AddToCartFeignService {

	@DeleteMapping("/delete-all-user-carts")
	ApiResponse<Void> deleteAllUserCarts(@RequestParam String addToCartIds, @RequestParam long userId);

	@GetMapping("/api/v1/elastic/user/get-user-cart")
	ApiResponse<ProductAddToCartModel> getUserCart(@RequestParam long addToCartId, @RequestParam long userId);
}
