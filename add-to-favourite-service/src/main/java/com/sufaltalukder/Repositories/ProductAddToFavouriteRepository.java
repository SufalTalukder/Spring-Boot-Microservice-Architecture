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
			    SELECT COUNT(f)
			    FROM ProductAddToFavouriteModel f
			    WHERE f.userInfo.userId = :userId
			      AND f.productInfo.productId = :productId
			""")
	long findCustomerByProductId(@Param("productId") long productId, @Param("userId") long userId);

	@Query("""
			    SELECT fm
			    FROM ProductAddToFavouriteModel fm
			    WHERE (:userId IS NULL OR fm.userInfo.userId = :userId)
			      AND (:productId IS NULL OR fm.productInfo.productId = :productId)
			    ORDER BY fm.favouriteCreatedAt DESC
			""")
	List<ProductAddToFavouriteModel> findUsersFavouritesByFilters(@Param("userId") Long userId,
			@Param("productId") Long productId);

	@Query("""
				 SELECT fm
				 FROM ProductAddToFavouriteModel fm
				 WHERE fm.addToFavouriteId = :addToFavouriteId
				 AND fm.userInfo.userId = :userId
			""")
	ProductAddToFavouriteModel findByIdAndUserId(@Param("addToFavouriteId") long addToFavouriteId,
			@Param("userId") long userId);

	@Query("""
				 SELECT COUNT(fm)
				 FROM ProductAddToFavouriteModel fm
				 WHERE fm.userInfo.userId = :userId
				 AND fm.productInfo.productId = :productId
			""")
	long countByUserIdAndProductId(@Param("userId") long userId, @Param("productId") long productId);

}