package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a DTO (Data Transfer Object) for a menu execution wastage report.
 *
 * @since 19-09-20223
 * @author Krushali Talaviya
 */
@Getter
@Setter
@AllArgsConstructor
public class AdminWastageReportDto {

	private Long menuItemId;

	private String menuItemName;

}