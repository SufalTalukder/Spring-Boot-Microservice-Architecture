package com.sufaltalukder.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sufaltalukder.Models.AuthLoginAuditModel;

public interface AuthLoginAuditRepository extends JpaRepository<AuthLoginAuditModel, Long> {

}
