package com.catering.dto.tenant.request;

import java.util.List;

import com.catering.dto.audit.IdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NameDto extends IdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private Double price;

	private Long totalItems;

	private List<PackageMenuItemCategoryDto> packageMenuItemCategoryList;

	public NameDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang) {
		setId(id);
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
	}

}