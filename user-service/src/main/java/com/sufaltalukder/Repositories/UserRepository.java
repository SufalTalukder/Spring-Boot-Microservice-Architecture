package com.sufaltalukder.Repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

	List<UserModel> findByPhoneNumber(String phoneNumber);

	Optional<UserModel> findByUserId(long userId);

	boolean existsByUserReferralCode(String userReferralCode);

	@Query("""
				SELECT u
				FROM UserModel u
				LEFT JOIN FETCH u.authUserInfo
				WHERE u.userId = :userId
			""")
	UserModel findUserDetailsByAuth(@Param("userId") long userId);

	@Query("""
				SELECT u
				FROM UserModel u
				LEFT JOIN FETCH u.authUserInfo
				ORDER BY u.userCreatedAt DESC
			""")
	List<UserModel> findAllUsersByAuth();

}
