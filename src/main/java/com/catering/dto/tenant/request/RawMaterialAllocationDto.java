package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialAllocationDto {

	private Long rawMaterialCategoryId;

	private String rawMaterialCategoryNameDefaultLang;

	private String rawMaterialCategoryNamePreferLang;

	private String rawMaterialCategoryNameSupportiveLang;

	private Long rawMaterialCategoryTypeId;

	public RawMaterialAllocationDto(Long rawMaterialCategoryId, String rawMaterialCategoryNameDefaultLang, String rawMaterialCategoryNamePreferLang, String rawMaterialCategoryNameSupportiveLang, Long rawMaterialCategoryTypeId) {
		this.rawMaterialCategoryId = rawMaterialCategoryId;
		this.rawMaterialCategoryNameDefaultLang = rawMaterialCategoryNameDefaultLang;
		this.rawMaterialCategoryNamePreferLang = rawMaterialCategoryNamePreferLang;
		this.rawMaterialCategoryNameSupportiveLang = rawMaterialCategoryNameSupportiveLang;
		this.rawMaterialCategoryTypeId = rawMaterialCategoryTypeId;
	}

}