package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonDataForDropDownDto extends OnlyIdDto {

	/**
	 * The default language name of the item category.
	 */
	private String nameDefaultLang;

	/**
	 * The preferred language name of the item category.
	 */
	private String namePreferLang;

	/**
	 * The supportive language name of the item category.
	 */
	private String nameSupportiveLang;

	public CommonDataForDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang) {
		this.setId(id);
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
	}

}