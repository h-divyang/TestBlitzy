package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseInputTransferHallReportDto {

	private String hallName;

	private Long inputTransferHallId;

	private LocalDateTime transferDate;

	private String rawMaterialName;

	private Double finalQty;

	private String finalQuantityMeasurement;

	private int decimalLimitQty;

	private String totalQty;

	// Constructor for mapping with items report
	public DateWiseInputTransferHallReportDto(String hallName, Long inputTransferHallId, LocalDateTime transferDate,
			String rawMaterialName, Double finalQty, String finalQuantityMeasurement, int decimalLimitQty) {
		this.hallName = hallName;
		this.inputTransferHallId = inputTransferHallId;
		this.transferDate = transferDate;
		this.rawMaterialName = rawMaterialName;
		this.finalQty = finalQty;
		this.finalQuantityMeasurement = finalQuantityMeasurement;
		this.decimalLimitQty = decimalLimitQty;
	}

	// Constructor for mapping without items report
	public DateWiseInputTransferHallReportDto(String hallName, Long inputTransferHallId, LocalDateTime transferDate,
			String totalQty) {
		this.hallName = hallName;
		this.inputTransferHallId = inputTransferHallId;
		this.transferDate = transferDate;
		this.totalQty = totalQty;
	}

}