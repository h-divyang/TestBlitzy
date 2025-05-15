package com.catering.dto.tenant.request;

import lombok.Getter;

@Getter
public class DishCostingDto extends NameDto {

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

	private Long functionId;

	public DishCostingDto(String nameDefaultLang, String namePreferLang, String nameSupportiveLang,
			Double chefLabourCharges, Double labourCharges, Double outsideAgencyCharges,
			Double extraExpenseCharges, Double totalCharges, Double totalRawMaterialCharges,
			Double totalAgencyCharges, Double totalGeneralFixCharges, Double totalCrockeryCharges, Double grandTotal,
			Double dishCosting, Long functionId) {
			setNameDefaultLang(nameDefaultLang);
			setNamePreferLang(namePreferLang);
			setNameSupportiveLang(nameSupportiveLang);
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
			this.functionId = functionId;
	}

}