package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.CheckOutHistoryModel;

@Repository
public interface CheckOutHistoryRepository extends JpaRepository<CheckOutHistoryModel, Long> {

	@Query("""
				SELECT c
				FROM CheckOutHistoryModel c
				LEFT JOIN FETCH c.userInfo
				WHERE c.checkOutHistoryId = :id
				AND c.userInfo.userId = :userId
			""")
	CheckOutHistoryModel findByCheckOutHistoryIdAndUserId(@Param("id") long id, @Param("userId") long userId);

	@Query("""
			    SELECT DISTINCT ch
			    FROM CheckOutHistoryModel ch
			    LEFT JOIN FETCH ch.authUserInfo
			    LEFT JOIN FETCH ch.userInfo
			""")
	List<CheckOutHistoryModel> findAllCheckoutHistories();

	@Query("""
				SELECT c
				FROM CheckOutHistoryModel c
				LEFT JOIN FETCH c.authUserInfo
				LEFT JOIN FETCH c.userInfo
				WHERE c.userInfo.userId = :userId
			""")
	Page<CheckOutHistoryModel> findCheckoutHistoriesOfUser(@Param("userId") long userId, Pageable pageable);

}
