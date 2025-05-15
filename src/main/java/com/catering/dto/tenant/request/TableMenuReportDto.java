package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TableMenuReportDto {

	/**
	 * The id of the order function.
	 */
	private Long functionId;

	/**
	 * The id of the order
	 */
	private Long orderId;

	/**
	 * The menu item category or classification of items related to the event.
	 */
	private String menuItemCategory;

	/**
	 * The menu item or substance used in the event.
	 */
	private String menuItem;

	private String companyUserName;

	private String mobileNumber;

}