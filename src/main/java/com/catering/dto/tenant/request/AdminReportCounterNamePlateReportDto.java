package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Menu Execution Name Plate Reports.
 * It encapsulates information related to the final material name.
 *
 * @author Krushali Talaviya
 * @since 2023-09-27
 */
@Getter
@Setter
public class AdminReportCounterNamePlateReportDto {

	private String menuItemName;

	/**
	 * Constructs a new MenuExecutionNamePlateReportDto with the specified final material name.
	 *
	 * @param menuItemName The name of the final material.
	 */
	public AdminReportCounterNamePlateReportDto(String menuItemName) {
		this.menuItemName = menuItemName;
	}

}