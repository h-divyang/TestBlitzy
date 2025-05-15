package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Data Transfer Object (DTO) for a manager's working report in menu preparation.
 *
 * This DTO encapsulates information related to a manager's working report, including event details,
 * manager information, food notes, and more.
 *
 * @author Krushali Talaviya
 * @since 2023-09-05
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuPreparationManagerMenuReportDto {

	/**
	 * The id of the order function.
	 */
	private Long functionId;

	/**
	 * The id of the order
	 */
	private Long orderId;

	/**
	 * The name of the function or type of event.
	 */
	private String functionName;

	/**
	 * The unique identifier for a person associated with the report.
	 */
	private Long person;

	private String functionAddress;

	private String functionNote;

	/**
	 * The start date of the event.
	 */
	private LocalDateTime date;

	/**
	 * The end date of the event.
	 */
	private LocalDateTime endDate;

	/**
	 * The menu item category or classification of items related to the event.
	 */
	private String menuItemCategory;

	/**
	 * The menu item or substance used in the event.
	 */
	private String menuItem;

	private String agencyName;

}