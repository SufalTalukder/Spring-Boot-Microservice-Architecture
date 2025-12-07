package com.sufaltalukder.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;

public interface ProductAddToFavouriteRepository extends JpaRepository<ProductAddToFavouriteModel, Long> {
	@Query("SELECT favourite FROM ProductAddToFavouriteModel favourite WHERE favourite.userId = :userId")
	Page<ProductAddToFavouriteModel> findAllFavouritesByUserId(long userId, PageRequest pageRequest);

	@Query("SELECT COUNT(favourite) FROM ProductAddToFavouriteModel favourite WHERE favourite.productId = :productId AND favourite.userId = :userId")
	long findCustomerByProductId(@Param("productId") long productId, @Param("userId") long userId);

	Page<ProductAddToFavouriteModel> findByUserId(long userId, Pageable pageable);
}