package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

	List<ProductModel> findByProductIdIn(List<Long> productIds);

}