package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;

public interface ProductAddToFavouriteRepository extends JpaRepository<ProductAddToFavouriteModel, Long> {

	@Query("""
			    SELECT f
			    FROM ProductAddToFavouriteModel f
			    WHERE f.userInfo.userId = :userId
			""")
	Page<ProductAddToFavouriteModel> findAllFavouritesByUserId(@Param("userId") long userId, Pageable pageable);

	@Query("""
			    SELECT f
			    FROM ProductAddToFavouriteModel f
			    WHERE f.userInfo.userId = :userId
			""")
	Page<ProductAddToFavouriteModel> findByUserId(@Param("userId") long userId, Pageable pageable);

	@Query("""
				SELECT fm
				FROM ProductAddToFavouriteModel fm
				LEFT JOIN FETCH fm.authUserInfo
				LEFT JOIN FETCH fm.userInfo
				LEFT JOIN FETCH fm.productInfo
			""")
	List<ProductAddToFavouriteModel> findUsersFavouritesByAuth();

	@Query("""
			    SELECT COUNT(f)
			    FROM ProductAddToFavouriteModel f
			    WHERE f.userInfo.userId = :userId
			      AND f.productInfo.productId = :productId
			""")
	long findCustomerByProductId(@Param("productId") long productId, @Param("userId") long userId);

}