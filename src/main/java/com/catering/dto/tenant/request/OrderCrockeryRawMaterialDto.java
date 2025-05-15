package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCrockeryRawMaterialDto extends RawMaterialDto {

	private Long agency;

	private LocalDateTime orderTime;

	private Long orderCrockeryId;

	private Double qty;

	private Long measurementId;

	private Long functionId;

	private LocalDateTime functionDate;

	private Long rawMaterialMeasurementId;

	private Double supplierRate;

	private Double price;

	private Long godown;

	public OrderCrockeryRawMaterialDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long agency, LocalDateTime orderTime,
			Long orderCrockeryId, Double qty, Long measurementId, Long functionId, LocalDateTime functionDate, Long rawMaterialMeasurementId, Double supplierRate, Double price, Long godown) {
		setId(id);
		setNameDefaultLang(nameDefaultLang);
		setNamePreferLang(namePreferLang);
		setNameSupportiveLang(nameSupportiveLang);
		this.agency = agency;
		this.orderTime = orderTime;
		this.orderCrockeryId = orderCrockeryId;
		this.qty = qty;
		this.measurementId = measurementId;
		this.functionId = functionId;
		this.functionDate = functionDate;
		this.rawMaterialMeasurementId = rawMaterialMeasurementId;
		this.supplierRate = supplierRate;
		this.price = price;
		this.godown = godown;
	}

}