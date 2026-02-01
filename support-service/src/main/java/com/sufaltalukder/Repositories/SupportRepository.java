package com.sufaltalukder.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sufaltalukder.Models.SupportModel;

@Repository
public interface SupportRepository extends JpaRepository<SupportModel, Long> {

	Optional<SupportModel> findBySupportIdAndUserInfo_UserId(long supportId, long userId);

	@Query("""
			    SELECT sm
			    FROM SupportModel sm
			    LEFT JOIN FETCH sm.authUserInfo
			    LEFT JOIN FETCH sm.userInfo
			    WHERE (:supportStatus IS NULL OR sm.supportStatus = :supportStatus)
			    ORDER BY sm.supportCreatedAt DESC
			""")
	List<SupportModel> findAllUserSupportsBySupportStatus(
			@Param("supportStatus") SupportModel.SupportStatus supportStatus);

}
