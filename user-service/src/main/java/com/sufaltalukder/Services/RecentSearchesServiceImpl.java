package com.sufaltalukder.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.RecentSearchesModel;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.RecentSearchesRepository;

@Service
public class RecentSearchesServiceImpl implements RecentSearchesService {

	@Autowired
	private RecentSearchesRepository recentSearchesRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Override
	public ApiResponse<RecentSearchesModel> addRecentSearches(RecentSearchesModel recentSearchesModel) {
		RecentSearchesModel saveData = recentSearchesRepository.save(recentSearchesModel);
		return new ApiResponse<>("success", "Recent searched added successfully.", saveData);
	}

	@Override
	public ApiResponse<List<RecentSearchesModel>> getRecentSearchesOfCustomer(long customerId) {
		Optional<RecentSearchesModel> isCustomerExists = recentSearchesRepository.findById(customerId);
		if (isCustomerExists.isEmpty()) {
			return new ApiResponse<>("not found", "Customer not found.", null);
		}
		List<RecentSearchesModel> getRecentSearches = recentSearchesRepository
				.findRecentSearchesByCustomer(isCustomerExists.get().getUserId());
		if (getRecentSearches.isEmpty()) {
			return new ApiResponse<>("not found", "Recent searches data not found.", null);
		}
		return new ApiResponse<>("success", "Recent searches data fetched successfully.", getRecentSearches);
	}

	@Override
	public ApiResponse<Void> deleteRecentSearches(long customerId, long recentSearchId) {
		Optional<AuthUserModel> isCustomerExists = authUserRepository.findById(customerId);
		if (isCustomerExists.isEmpty()) {
			return new ApiResponse<>("not found", "Customer not found.", null);
		}
		Optional<RecentSearchesModel> isRecentSearchesExists = recentSearchesRepository.findById(recentSearchId);
		if (isRecentSearchesExists.isEmpty()) {
			return new ApiResponse<>("not found", "Recent searched data not found.", null);
		}
		long findCustomerByRecentSearchId = recentSearchesRepository.findUserIdByRecentSearchId(recentSearchId);
		if (isCustomerExists.get().getAuthUserId() != findCustomerByRecentSearchId) {
			return new ApiResponse<>("not found", "Customer not found for recent search id.", null);
		}
		recentSearchesRepository.delete(isRecentSearchesExists.get());
		return new ApiResponse<>("success", "Recent searched data deleted successfully.", null);
	}
}