package com.sufaltalukder.Repositories;

import java.util.List;

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
			    WHERE a.authUserId = :authUserId
			    ORDER BY a.actionLogCreatedAt DESC
			""")
	List<ActionLogModel> findAllActionLogs(@Param("authUserId") long authUserId);

	@Query("""
			    SELECT a
			    FROM ActionLogModel a
			    WHERE a.authUserId = :rqstAuthUserId
			    ORDER BY a.actionLogCreatedAt DESC
			""")
	List<ActionLogModel> findAllUserActionLogs(@Param("rqstAuthUserId") long rqstAuthUserId);
}
