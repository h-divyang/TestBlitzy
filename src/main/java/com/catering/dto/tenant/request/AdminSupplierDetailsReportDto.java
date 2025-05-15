package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for storing administrative details
 * related to supplier reports. It encapsulates information such as company details,
 * event information, customer details, and supplier details.
 *
 * <p><strong>Author:</strong> Krushali Talaviya</p>
 * <p><strong>Since:</strong> 2023-12-25</p>
 *
 * @see java.io.Serializable
 */
@Getter
@Setter
@AllArgsConstructor
public class AdminSupplierDetailsReportDto {

	private Long orderFunctionId;

	private String functionName;

	private Long person;

	private String functionAddress;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private String agencyName;

	private String agencyCategory;

	private String supplierNumber;

}