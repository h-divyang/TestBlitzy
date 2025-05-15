package com.catering.dto.tenant.request;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a FunctionTypeDto in the tenant package.
 * It extends the AuditIdDto class and defines the properties of a function master for DTO (Data Transfer Object) purposes.
 * 
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderForDashboardFunctionTypeDto extends CommonMultiLanguageWithoutAuditDto {

	private LocalTime time;

}