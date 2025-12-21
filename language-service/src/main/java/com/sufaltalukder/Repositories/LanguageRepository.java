package com.sufaltalukder.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.LanguageModel;

import feign.Param;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageModel, Long> {

	LanguageModel findByLanguageName(String languageName);

	@Query("""
				SELECT l
				FROM LanguageModel l
				LEFT JOIN FETCH l.authUserInfo
			""")
	List<LanguageModel> findAllLanguages();

	@Query("""
				SELECT l
				FROM LanguageModel l
				LEFT JOIN FETCH l.authUserInfo
				WHERE l.languageId = :languageId
			""")
	Optional<LanguageModel> findByLanguageIdOfAuth(@Param("languageId") long languageId);

}
