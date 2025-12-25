package com.sufaltalukder.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.SubCategoryModel;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryModel, Long> {

	@Query(value = "SELECT * FROM sub_category_tbl WHERE categoryId = :categoryId", nativeQuery = true)
	List<SubCategoryModel> findAllByCategoryId(@Param("categoryId") long categoryId);

	SubCategoryModel findBySubCategoryName(String subCategoryName);

	@Query("""
				SELECT sc
				FROM SubCategoryModel sc
				LEFT JOIN FETCH sc.authUserInfo
			""")
	List<SubCategoryModel> findAllSubCategories();

	@Query("""
				SELECT sc
				FROM SubCategoryModel sc
				LEFT JOIN FETCH sc.authUserInfo
				WHERE sc.subCategoryId = :subCategoryId
			""")
	Optional<SubCategoryModel> findSubcategoryByIdOfAuth(@Param("subCategoryId") long subCategoryId);
}