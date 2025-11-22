package com.sufaltalukder.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.AppBannerModel;

@Repository
public interface AppBannerRepository extends JpaRepository<AppBannerModel, Long> {

}
