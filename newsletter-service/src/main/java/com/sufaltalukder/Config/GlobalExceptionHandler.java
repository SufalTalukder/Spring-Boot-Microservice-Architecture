package com.sufaltalukder.Config;

import com.sufaltalukder.Models.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FeignCustomException.class)
	public ResponseEntity<ApiResponse<?>> handleFeignError(FeignCustomException ex) {

		ApiResponse<?> apiResponse = ex.getApiResponse();

		HttpStatus status = switch (ex.getStatus()) {
		case 400 -> HttpStatus.BAD_REQUEST;
		case 401 -> HttpStatus.UNAUTHORIZED;
		case 403 -> HttpStatus.FORBIDDEN;
		case 404 -> HttpStatus.NOT_FOUND;
		default -> HttpStatus.INTERNAL_SERVER_ERROR;
		};

		return new ResponseEntity<>(apiResponse, status);
	}
}
