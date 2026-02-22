package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.NewsletterModel;

public class NewsletterMapper {

	public static NewsletterDTO toDTO(NewsletterModel entity) {
		if (entity == null) {
			return null;
		}

		return new NewsletterDTO(entity.getNewsletterId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				UserMapper.toDTO(entity.getUserInfo()), entity.getNewsletterToggle(), entity.getNewsletterCreatedAt(),
				entity.getNewsletterUpdatedAt());
	}
}