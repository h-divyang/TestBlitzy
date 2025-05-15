package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChefOrOutsideLabourReportParams {

	private Integer langType;

	private String langCode;

	private Long[] contactId;

	private Long[] functionId;

	private Long[] menuItemId;

}