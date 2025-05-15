package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing the request for order invoice functions.
 * Extends {@link AuditIdDto} to include audit-related fields.
 *
 * The class encapsulates information required to create or update order invoice functions,
 * including details such as the associated order invoice and order function identifiers,
 * the number of persons, additional charges (extra), rate, and the name of the charges.
 *
 * This DTO is typically used to transfer order invoice function request data between different layers of the application,
 * especially during the creation or update of order invoice functions.
 *
 * The class includes a no-argument constructor for creating instances without specifying initial values.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderInvoiceFunctionRequestDto  extends AuditIdDto {

	private Long orderInvoiceId;

	private Long orderFunctionId;

	private Integer person;

	private Double extra;

	private Double rate;

	private String chargesName;

	private LocalDateTime date;

}