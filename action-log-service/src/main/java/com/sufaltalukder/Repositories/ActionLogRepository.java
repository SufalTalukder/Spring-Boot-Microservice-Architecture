package com.sufaltalukder.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.ActionLogModel;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLogModel, Long> {

	@Query("""
			    SELECT a
			    FROM ActionLogModel a
			    ORDER BY a.actionLogCreatedAt DESC
			""")
	Page<ActionLogModel> findAllActionLogs(Pageable pageable);

	@Query("""
			    SELECT a
			    FROM ActionLogModel a
			    WHERE a.userId = :userId
			    ORDER BY a.actionLogCreatedAt DESC
			""")
	Page<ActionLogModel> findAllUserActionLogs(@Param("userId") long userId, Pageable pageable);
}
