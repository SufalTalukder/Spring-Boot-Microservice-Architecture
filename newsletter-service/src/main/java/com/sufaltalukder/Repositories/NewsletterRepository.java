package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.NewsletterModel;

@Repository
public interface NewsletterRepository extends JpaRepository<NewsletterModel, Long> {

	@Query("""
			    SELECT COUNT(n)
			    FROM NewsletterModel n
			    WHERE n.userInfo.userId = :userId
			""")
	long existsNewsletterByUserId(@Param("userId") long userId);

	@Query("""
				SELECT n
				FROM NewsletterModel n
				LEFT JOIN FETCH n.authUserInfo
				LEFT JOIN FETCH n.userInfo
			""")
	List<NewsletterModel> findAllNewsletters();

}
