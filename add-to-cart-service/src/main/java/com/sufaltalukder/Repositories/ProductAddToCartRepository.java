package com.sufaltalukder.Repositories;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.ProductAddToCartModel;

@Repository
public interface ProductAddToCartRepository extends JpaRepository<ProductAddToCartModel, Long> {

	@Query(value = "SELECT add_to_cart_id FROM add_to_cart_tbl WHERE user_id = :userId", nativeQuery = true)
	long findCartIdByUserId(@Param("userId") long userId);

	@Query("SELECT cart FROM ProductAddToCartModel cart WHERE cart.userId = :userId")
	Page<ProductAddToCartModel> findAllCartsByUserId(@Param("userId") long userId, PageRequest pageRequest);

	@Query("SELECT COUNT(cart) FROM ProductAddToCartModel cart WHERE cart.productId = :productId AND cart.userId = :userId")
	long findUserByProductId(@Param("productId") long productId, @Param("userId") long userId);

	Page<ProductAddToCartModel> findByUserId(long userId, Pageable pageable);

	@Query("SELECT cart FROM ProductAddToCartModel cart WHERE cart.addToCartId = :eachCartId AND cart.userId = :userId")
	ProductAddToCartModel findCartByUserId(@Param("eachCartId") long eachCartId, @Param("userId") long userId);

	@Query("SELECT cart FROM ProductAddToCartModel cart WHERE cart.userId = :userId")
	Page<ProductAddToCartModel> findAllPurchasesByUserId(@Param("userId") long userId, Pageable pageable);
}
