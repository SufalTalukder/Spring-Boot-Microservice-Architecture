package com.sufaltalukder.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.CategoryModel;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

	Optional<CategoryModel> findByCategoryName(String categoryName);

}
