package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.ProductAddToCartModel;

@Repository
public interface ProductAddToCartRepository extends JpaRepository<ProductAddToCartModel, Long> {

	@Query("""
			    SELECT COUNT(cart)
			    FROM ProductAddToCartModel cart
			    WHERE cart.userInfo.userId = :userId
			      AND cart.productInfo.productId = :productId
			""")
	long existsByUserIdAndProductId(@Param("userId") long userId, @Param("productId") long productId);

	@Query("""
				SELECT cart
				FROM ProductAddToCartModel cart
				WHERE cart.userInfo.userId = :userId
			""")
	Page<ProductAddToCartModel> findUserByUserId(@Param("userId") long userId, Pageable pageable);

	@Query("""
				SELECT cart
				FROM ProductAddToCartModel cart
				WHERE cart.addToCartId = :eachCartId
				AND cart.userInfo.userId = :userId
			""")
	ProductAddToCartModel findCartByUserId(@Param("eachCartId") long eachCartId, @Param("userId") long userId);

	@Query("""
				SELECT c
				FROM ProductAddToCartModel c
				LEFT JOIN FETCH c.authUserInfo
				LEFT JOIN FETCH c.userInfo
				LEFT JOIN FETCH c.productInfo
			""")
	List<ProductAddToCartModel> findAllCarts();

	@Query("""
				SELECT c
				FROM ProductAddToCartModel c
				LEFT JOIN FETCH c.authUserInfo
				LEFT JOIN FETCH c.userInfo
				LEFT JOIN FETCH c.productInfo
				WHERE c.userInfo.userId = :userId
			""")
	List<ProductAddToCartModel> findUserCartsByUserId(@Param("userId") long userId);
}
