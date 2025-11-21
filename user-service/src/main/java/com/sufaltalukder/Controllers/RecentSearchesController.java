package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.RecentSearchesModel;
import com.sufaltalukder.Services.RecentSearchesService;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class RecentSearchesController {

	@Autowired
	private RecentSearchesService recentSearchesService;

	@PostMapping("/add-recent-search")
	public ResponseEntity<ApiResponse<RecentSearchesModel>> addRecentSearches(
			@RequestBody RecentSearchesModel recentSearchesModel) {
		ApiResponse<RecentSearchesModel> response = recentSearchesService.addRecentSearches(recentSearchesModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/get-recent-searches")
	public ResponseEntity<ApiResponse<List<RecentSearchesModel>>> getRecentSearchesOfCustomer(
			@RequestParam long customerId) {
		ApiResponse<List<RecentSearchesModel>> response = recentSearchesService.getRecentSearchesOfCustomer(customerId);
		if (!"success".equals(response.getStatus())) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/delete-recent-search")
	public ResponseEntity<ApiResponse<Void>> deleteRecentSearches(@RequestParam long customerId,
			@RequestParam long recentSearchId) {
		ApiResponse<Void> response = recentSearchesService.deleteRecentSearches(customerId, recentSearchId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}