package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

	List<UserModel> findByPhoneNumber(String phoneNumber);

	Optional<UserModel> findByUserId(long userId);

	boolean existsByUserReferralCode(String userReferralCode);

}
