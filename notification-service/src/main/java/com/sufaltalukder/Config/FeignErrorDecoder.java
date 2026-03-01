package com.sufaltalukder.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sufaltalukder.Models.ApiResponse;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ErrorDecoder defaultDecoder = new Default();

	@Override
	public Exception decode(String methodKey, Response response) {

		String responseBody = "";

		try {
			if (response.body() != null) {
				responseBody = Util.toString(response.body().asReader());
			}
		} catch (Exception ignored) {
		}

		// Try converting to ApiResponse
		try {
			if (!responseBody.isEmpty()) {
				ApiResponse<?> apiError = objectMapper.readValue(responseBody, ApiResponse.class);

				return new FeignCustomException(apiError, response.status());
			}
		} catch (Exception ignored) {
		}

		return new FeignCustomException(new ApiResponse<>("error", "Unknown upstream error", null), response.status());
	}
}
