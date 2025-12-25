package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

	@Query("""
			    SELECT p FROM ProductModel p
			    LEFT JOIN p.languageInfo l
			    LEFT JOIN p.subCategoryInfo s
			    LEFT JOIN p.categoryInfo c
			    WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(p.productBrand) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR CAST(p.productCode AS string) LIKE CONCAT('%', :q, '%')
			       OR CAST(p.productPrice AS string) LIKE CONCAT('%', :q, '%')
			       OR LOWER(s.subCategoryName) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :q, '%'))
			""")
	List<ProductModel> findSearchedResultsByQuery(@Param("q") String q);

	@Query(value = "SELECT product_tbl.product_price FROM product_tbl WHERE product_tbl.product_id = :productId", nativeQuery = true)
	Long findProductPriceByProductId(@Param("productId") long productId);

	ProductModel findByProductName(String productName);

	List<ProductModel> findByProductNameIn(List<String> productNames);

	@Query("""
				SELECT p
				FROM ProductModel p
				LEFT JOIN FETCH p.authUserInfo
				LEFT JOIN FETCH p.languageInfo
				LEFT JOIN FETCH p.categoryInfo
				LEFT JOIN FETCH p.subCategoryInfo
				WHERE p.languageInfo = :languageId
			""")
	List<ProductModel> findProductsByLanguageId(@Param("languageId") long languageId);

	@Query("""
				SELECT p
				FROM ProductModel p
				LEFT JOIN FETCH p.authUserInfo
				LEFT JOIN FETCH p.languageInfo
				LEFT JOIN FETCH p.categoryInfo
				LEFT JOIN FETCH p.subCategoryInfo
				WHERE p.categoryInfo = :categoryId
			""")
	List<ProductModel> findProductsByCategoryId(@Param("categoryId") long categoryId);

	@Query("""
				SELECT p
				FROM ProductModel p
				LEFT JOIN FETCH p.authUserInfo
				LEFT JOIN FETCH p.languageInfo
				LEFT JOIN FETCH p.categoryInfo
				LEFT JOIN FETCH p.subCategoryInfo
				WHERE p.subCategoryInfo = :subCategoryId
			""")
	List<ProductModel> findProductsBySubCategoryId(@Param("subCategoryId") long subCategoryId);

	Optional<ProductModel> findByProductId(long productId);

	@Query("""
				SELECT p
				FROM ProductModel p
				LEFT JOIN FETCH p.authUserInfo
				LEFT JOIN FETCH p.languageInfo
				LEFT JOIN FETCH p.categoryInfo
				LEFT JOIN FETCH p.subCategoryInfo
			""")
	List<ProductModel> findAllProducts();

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

}