package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sufaltalukder.Models.AuthLoginAuditModel;

public interface AuthLoginAuditRepository extends JpaRepository<AuthLoginAuditModel, Long> {

	@Query("""
				SELECT am
				FROM AuthLoginAuditModel am
				LEFT JOIN FETCH am.authUserInfo
				ORDER BY am.createdAt DESC
			""")
	List<AuthLoginAuditModel> findAllAuditDetails();

	@Query("""
				SELECT am
				FROM AuthLoginAuditModel am
				LEFT JOIN FETCH am.authUserInfo
				WHERE am.authLoginAuditId = :authLoginAuditId
			""")
	AuthLoginAuditModel findLoginAuditDetailsById(@Param("authLoginAuditId") long authLoginAuditId);

}
