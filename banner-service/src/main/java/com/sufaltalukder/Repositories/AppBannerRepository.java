package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.AppBannerModel;

@Repository
public interface AppBannerRepository extends JpaRepository<AppBannerModel, Long> {

	@Query("""
			    SELECT ab
			    FROM AppBannerModel ab
			    LEFT JOIN FETCH ab.authUserInfo
			    ORDER BY ab.appBannerCreatedAt DESC
			""")
	List<AppBannerModel> findAllBanners();
}
