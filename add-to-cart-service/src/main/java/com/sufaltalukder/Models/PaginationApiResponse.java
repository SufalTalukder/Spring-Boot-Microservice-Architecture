package com.sufaltalukder.Models;

import lombok.Data;

@Data
public class PaginationApiResponse<T> {
	private String status;
	private String message;
	private T content;
	private int currentPage;
	private int pageSize;
	private long totalRecords;

	// With pagination data response
	public PaginationApiResponse(String status, String message, T content, int currentPage, int pageSize,
			long totalRecords) {
		this.status = status;
		this.message = message;
		this.content = content;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
	}

}
