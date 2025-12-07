package com.sufaltalukder.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.NewsletterModel;

@Repository
public interface NewsletterRepository extends JpaRepository<NewsletterModel, Long> {

	NewsletterModel findByUserId(long userId);

}
