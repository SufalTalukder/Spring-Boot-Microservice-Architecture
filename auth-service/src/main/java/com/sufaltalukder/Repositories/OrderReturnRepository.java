package com.sufaltalukder.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sufaltalukder.Models.OrderReturnModel;

public interface OrderReturnRepository extends JpaRepository<OrderReturnModel, Long> {

	Optional<OrderReturnModel> findByCheckOutHistoryId(long checkOutHistoryId);

	Page<OrderReturnModel> findByUserId(long userId, Pageable pageable);
}
