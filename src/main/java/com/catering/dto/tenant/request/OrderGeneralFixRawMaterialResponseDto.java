package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGeneralFixRawMaterialResponseDto extends IdDto {

	private Long rawMaterialId;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private Long agency;

	private LocalDateTime orderTime;

	private Double qty;

	private Long measurementId;

	private Long orderFunctionId;
	
	private LocalDateTime functionDate;

	private Long rawMaterialMeasurementId;

	private Double supplierRate;

	private Double price;
	
	private Long godown;

	public OrderGeneralFixRawMaterialResponseDto(Long rawMaterialId, String nameDefaultLang,
			String namePreferLang, String nameSupportiveLang, Long agency, LocalDateTime orderTime, Long orderGeneralFixRawMaterialId, Double qty, Long measurementId, Long orderFunctionId, LocalDateTime functionDate, Long rawMaterialMeasurementId, Double supplierRate, Double price, Long godown) {
		setId(orderGeneralFixRawMaterialId);
		this.rawMaterialId = rawMaterialId;
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
		this.agency = agency;
		this.orderTime = orderTime;
		this.qty = qty;
		this.measurementId = measurementId;
		this.orderFunctionId = orderFunctionId;
		this.functionDate = functionDate;
		this.rawMaterialMeasurementId = rawMaterialMeasurementId;
		this.supplierRate = supplierRate;
		this.price = price;
		this.godown = godown;
	}

}