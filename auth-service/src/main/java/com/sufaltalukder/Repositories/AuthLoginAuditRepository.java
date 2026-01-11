package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sufaltalukder.Models.AuthLoginAuditModel;

public interface AuthLoginAuditRepository extends JpaRepository<AuthLoginAuditModel, Long> {

	@Query("""
				SELECT am
				FROM AuthLoginAuditModel am
				LEFT JOIN FETCH am.authUserInfo
			""")
	List<AuthLoginAuditModel> findAllAuditDetails();

}
