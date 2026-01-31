package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

	ProductModel findByProductName(String productName);

	List<ProductModel> findByProductNameIn(List<String> productNames);

	Optional<ProductModel> findByProductId(long productId);

	@Query("""
				SELECT p
				FROM ProductModel p
				LEFT JOIN FETCH p.authUserInfo
				LEFT JOIN FETCH p.languageInfo
				LEFT JOIN FETCH p.categoryInfo
				LEFT JOIN FETCH p.subCategoryInfo
				WHERE p.productId = :productId
			""")
	Optional<ProductModel> findProductByIdOfACSL(@Param("productId") long productId);

	@Query("""
			    SELECT p
			    FROM ProductModel p
			    WHERE (:categoryId IS NULL OR p.categoryInfo.categoryId = :categoryId)
			      AND (:subCategoryId IS NULL OR p.subCategoryInfo.subCategoryId = :subCategoryId)
			      AND (:languageId IS NULL OR p.languageInfo.languageId = :languageId)
			    ORDER BY p.productCreatedAt DESC
			""")
	List<ProductModel> findProductsByFilters(@Param("categoryId") Long categoryId,
			@Param("subCategoryId") Long subCategoryId, @Param("languageId") Long languageId);
}