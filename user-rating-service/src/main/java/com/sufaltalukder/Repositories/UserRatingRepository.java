package com.sufaltalukder.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.UserRatingModel;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRatingModel, Long> {

	UserRatingModel findByUserRatingId(long userRatingId);

	@Query("SELECT r FROM UserRatingModel r WHERE r.productId = :productId ORDER BY r.userRatingCreatedAt DESC")
	Page<UserRatingModel> findAllRatingsOfProduct(@Param("productId") long productId, Pageable pageable);

	@Query("SELECT r FROM UserRatingModel r WHERE r.userId = :userId AND r.productId = :productId AND r.userRatingId = :userRatingId")
	UserRatingModel findUserRatingOfProductByUserId(@Param("userId") long userId, @Param("productId") long productId,
			@Param("userRatingId") long userRatingId);

}
