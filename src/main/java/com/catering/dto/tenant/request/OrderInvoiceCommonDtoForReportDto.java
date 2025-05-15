package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Data Transfer Object (DTO) for generating reports based on order invoices.
 * This class encapsulates common information to be included in the report.
 *
 * The information includes details such as party name, venue, party number, and party GST number.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderInvoiceCommonDtoForReportDto {

	/**
	 * The name of the party associated with the order invoice.
	 */
	private String customerName;

	/**
	 * The venue where the transaction or event associated with the order invoice took place.
	 */
	private String venue;

	/**
	 * The unique party number associated with the order invoice.
	 */
	private String customerNumber;

	/**
	 * The party GST number for the particular party.
	 */
	private String customerGSTNumber;

}