package com.sufaltalukder.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.AuthUserRefreshTokenModel;

@Repository
public interface AuthUserRefreshTokenRepository extends JpaRepository<AuthUserRefreshTokenModel, Long> {

	Optional<AuthUserRefreshTokenModel> findByRefreshTokenAndIsRevokedFalse(String refreshToken);

}
