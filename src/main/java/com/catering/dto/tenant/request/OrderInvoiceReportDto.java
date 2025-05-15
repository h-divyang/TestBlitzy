package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a line item in the order invoice report.
 *
 * The class encapsulates information about a specific charge or item in the order invoice report,
 * including details such as charges name, quantity, rate, and the calculated amount for that charge.
 *
 * This DTO is typically used to transfer order invoice report data between different layers of the application,
 * especially when generating reports.
 *
 * The class includes a constructor for initializing its fields.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderInvoiceReportDto {

	private Long orderId;

	private LocalDateTime date;

	private String chargesName;

	private Long quantity;

	private Double rate;

	private Double amount;

	private String sac;

	private Long extra;

	private Boolean isFunction;

}