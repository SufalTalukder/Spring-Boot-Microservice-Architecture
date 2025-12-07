package com.sufaltalukder.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.NewsletterModel;

@Repository
public interface NewsletterRepository extends JpaRepository<NewsletterModel, Long> {

	NewsletterModel findByUserId(long userId);

//	@Query("SELECT nr FROM NewsletterModel nr WHERE nr.userId = :userId")
//	NewsletterModel findNewsletterByUserId(@Param("userId") long userId);

}
