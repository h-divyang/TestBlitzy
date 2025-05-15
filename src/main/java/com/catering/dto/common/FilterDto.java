package com.catering.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDto {

	private String currentPage;

	private String sizePerPage;

	private String sortBy;

	private String sortDirection;

	private String query;

}