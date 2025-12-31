package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.AuthUserModel;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserModel, Long> {

	AuthUserModel findByAuthUserEmailAddress(String authUserEmailAddress);

	AuthUserModel findByAuthUserEmailAddressAndAuthUserPassword(String authUserEmailAddress, String authUserPassword);

	@Query("""
			    SELECT au
			    FROM AuthUserModel au
			    LEFT JOIN FETCH au.actionByUserInfo
			""")
	List<AuthUserModel> findAllAuthUsers();

}