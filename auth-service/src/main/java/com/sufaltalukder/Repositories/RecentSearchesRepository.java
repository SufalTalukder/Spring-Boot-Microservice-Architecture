package com.sufaltalukder.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.RecentSearchesModel;

@Repository
public interface RecentSearchesRepository extends JpaRepository<RecentSearchesModel, Long> {

	@Query(value = "SELECT * FROM recent_search_tbl WHERE user_id = :userId ORDER BY created_at DESC LIMIT 4", nativeQuery = true)
	List<RecentSearchesModel> findRecentSearchesByCustomer(@Param("userId") long userId);

	@Query(value = "SELECT userId FROM recent_search_tbl WHERE recent_search_id = :recentSearchId", nativeQuery = true)
	long findUserIdByRecentSearchId(long recentSearchId);

}