package com.sufaltalukder.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.CategoryModel;

import feign.Param;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

	CategoryModel findByCategoryName(String categoryName);

	@Query("""
				SELECT c
				FROM CategoryModel c
				LEFT JOIN FETCH c.authUserInfo
				WHERE c.categoryId = :categoryId
			""")
	Optional<CategoryModel> findCategoryByIdOfAuth(@Param("categoryId") long categoryId);

	@Query("""
				SELECT c
				FROM CategoryModel c
				LEFT JOIN FETCH c.authUserInfo
				ORDER BY c.categoryCreatedAt DESC
			""")
	List<CategoryModel> findAllCategories();

}
