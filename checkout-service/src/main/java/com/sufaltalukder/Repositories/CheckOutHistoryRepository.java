package com.sufaltalukder.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.CheckOutHistoryModel;

@Repository
public interface CheckOutHistoryRepository extends JpaRepository<CheckOutHistoryModel, Long> {

	Page<CheckOutHistoryModel> findByUserId(long userId, Pageable pageable);

	@Query("""
			SELECT c FROM CheckOutHistoryModel c
			LEFT JOIN FETCH c.userInfo
			WHERE c.checkOutHistoryId = :id AND c.userId = :userId
			""")
	CheckOutHistoryModel findByCheckOutHistoryIdAndUserId(@Param("id") long id, @Param("userId") long userId);

}
