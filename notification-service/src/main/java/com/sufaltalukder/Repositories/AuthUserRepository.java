package com.sufaltalukder.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.AuthUserModel;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserModel, Long> {

	AuthUserModel findByAuthUserEmailAddress(String authUserEmailAddress);

	AuthUserModel findByAuthUserEmailAddressAndAuthUserPassword(String authUserEmailAddress, String authUserPassword);

}