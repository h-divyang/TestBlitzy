package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderCrockeryDto extends CommonDataForDropDownDto {

	private Long rawMaterialId;

	private Long rawMaterialAgencyId;

	private Long crockeryAgencyId;

	private LocalDateTime orderTime;

	private Double qty;

	private Long measurementId;

	private LocalDateTime functionDate;

	private Long rawMaterialMeasurementId;

	private Double supplierRate;

	private Double price;

	private Long godown;

	public GetOrderCrockeryDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long rawMaterialId, Long rawMaterialAgencyId, Long crockeryAgencyId, 
			LocalDateTime orderTime, Double qty, Long measurementId, LocalDateTime functionDate, Long rawMaterialMeasurementId, Double supplierRate, Double price, Long godown) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.rawMaterialId = rawMaterialId;
		this.rawMaterialAgencyId = rawMaterialAgencyId;
		this.crockeryAgencyId = crockeryAgencyId;
		this.orderTime = orderTime;
		this.qty = qty;
		this.measurementId = measurementId;
		this.functionDate = functionDate;
		this.rawMaterialMeasurementId = rawMaterialMeasurementId;
		this.supplierRate = supplierRate;
		this.price = price;
		this.godown = godown;
	}

}