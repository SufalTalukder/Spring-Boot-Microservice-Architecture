package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.NewsletterModel;

public class NewsletterMapper {

	public static NewsletterDTO toDTO(NewsletterModel entity) {
		if (entity == null) {
			return null;
		}

		return new NewsletterDTO(entity.getNewsletterId(), entity.getAuthUserInfo(), entity.getUserInfo(),
				entity.getNewsletterToggle(), entity.getNewsletterCreatedAt(), entity.getNewsletterUpdatedAt());
	}

	public static NewsletterModel toEntity(NewsletterDTO dto) {
		if (dto == null) {
			return null;
		}

		NewsletterModel entity = new NewsletterModel();
		entity.setNewsletterId(dto.getNewsletterId());
		entity.setAuthUserInfo(dto.getAuthUserInfo());
		entity.setUserInfo(dto.getUserInfo());
		entity.setNewsletterToggle(dto.getNewsletterToggle());

		return entity;
	}
}