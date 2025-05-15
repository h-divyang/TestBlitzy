package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DishCostingReportDto extends IdDto {

	private String functionName;

	private Double chefLabourCharges;

	private Double labourCharges;

	private Double outsideAgencyCharges;

	private Double extraExpenseCharges;

	private Double totalCharges;

	private Double totalRawMaterialCharges;

	private Double totalAgencyCharges;

	private Double totalGeneralFixCharges;

	private Double totalCrockeryCharges;

	private Double grandTotal;

	private Double dishCosting;

	private LocalDateTime date;

	private LocalDateTime endDate;

	public DishCostingReportDto(Long id, String functionName,
			Double chefLabourCharges, Double labourCharges, Double outsideAgencyCharges,
			Double extraExpenseCharges, Double totalCharges, Double totalRawMaterialCharges,
			Double totalAgencyCharges, Double totalGeneralFixCharges, Double totalCrockeryCharges, Double grandTotal,
			Double dishCosting, LocalDateTime date, LocalDateTime endDate) {
			setId(id);
			this.functionName = functionName;
			this.chefLabourCharges = chefLabourCharges;
			this.labourCharges = labourCharges;
			this.outsideAgencyCharges = outsideAgencyCharges;
			this.extraExpenseCharges = extraExpenseCharges;
			this.totalCharges = totalCharges;
			this.totalRawMaterialCharges = totalRawMaterialCharges;
			this.totalAgencyCharges = totalAgencyCharges;
			this.totalGeneralFixCharges = totalGeneralFixCharges;
			this.totalCrockeryCharges = totalCrockeryCharges;
			this.grandTotal = grandTotal;
			this.dishCosting = dishCosting;
			this.date = date;
			this.endDate = endDate;
	}

}