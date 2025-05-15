package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllDataReportRawMaterialReportDto extends OnlyIdDto {

	private String rawMaterial;

	private String rawMaterialCategory;

	public AllDataReportRawMaterialReportDto(Long id, String rawMaterialCategory, String rawMaterial) {
		this.setId(id);
		this.rawMaterialCategory = rawMaterialCategory;
		this.rawMaterial = rawMaterial;
	}

}