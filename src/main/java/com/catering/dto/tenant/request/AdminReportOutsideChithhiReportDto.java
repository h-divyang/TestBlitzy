package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReportOutsideChithhiReportDto extends LabourAndAgencyChefLabourChithhiReportDto {

	private Double quantity;

	private String unit;

	private Integer decimalLimitQty;

	public AdminReportOutsideChithhiReportDto(String agencyName,
			String agencyNumber, String managerName, String managerNumber, LocalDateTime orderDate, String venue,
			String menuItemName, String timePeriod, Double quantity, String unit, Integer decimalLimitQty) {
		super(agencyName, agencyNumber, managerName, managerNumber, orderDate, null, venue,
				menuItemName, null, null, null, null, null, timePeriod, null, null);
		this.quantity = quantity;
		this.unit = unit;
		this.decimalLimitQty = decimalLimitQty;
	}

}