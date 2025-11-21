package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.RecentSearchesModel;

public interface RecentSearchesService {

	ApiResponse<RecentSearchesModel> addRecentSearches(RecentSearchesModel recentSearchesModel);

	ApiResponse<Void> deleteRecentSearches(long customerId, long recentSearchId);

	ApiResponse<List<RecentSearchesModel>> getRecentSearchesOfCustomer(long customerId);
}