package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.domain.*;
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
			    LEFT JOIN p.categorInfo c
			    WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(p.productBrand) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR CAST(p.productCode AS string) LIKE CONCAT('%', :q, '%')
			       OR CAST(p.productPrice AS string) LIKE CONCAT('%', :q, '%')
			       OR LOWER(s.subCategoryName) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :q, '%'))
			""")
	List<ProductModel> findSearchedResultsByQuery(@Param("q") String q);

	@Query(value = """
			    SELECT p.product_price
			    FROM product_tbl p
			    WHERE p.product_id = :productId
			""", nativeQuery = true)
	Double findProductPriceByProductId(@Param("productId") long productId);

	ProductModel findByProductName(String productName);

	List<ProductModel> findByProductNameIn(List<String> productNames);

	Page<ProductModel> findByLanguageId(Long languageId, Pageable pageable);

	Page<ProductModel> findByCategoryId(Long categoryId, Pageable pageable);

	Page<ProductModel> findBySubCategoryId(Long subCategoryId, Pageable pageable);

	Optional<ProductModel> findByProductId(long productId);

}