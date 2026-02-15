package com.sufaltalukder.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.LanguageModel;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageModel, Long> {

	LanguageModel findByLanguageName(String languageName);

}
