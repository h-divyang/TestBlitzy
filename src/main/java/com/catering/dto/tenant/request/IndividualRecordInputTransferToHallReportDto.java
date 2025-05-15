package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualRecordInputTransferToHallReportDto extends OnlyIdDto {

	private LocalDate transferDate;

	private String hallName;

	private String rawMaterial;

	private Double weight;

	private String measurement;

	private Integer decimalLimitQty;

	public IndividualRecordInputTransferToHallReportDto(Long id, LocalDate transferDate,
			String hallName, String rawMaterial, Double weight, String measurement, Integer decimalLimitQty) {
		this.setId(id);
		this.transferDate = transferDate;
		this.hallName = hallName;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
	}

}