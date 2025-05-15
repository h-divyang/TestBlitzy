package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Amit Chauhan
 * @since 2024-01-31
 */
@Getter
@Setter
public class RawMaterialCategoryDirectOrderDto extends OnlyIdDto {

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

	/**
	 * Constructs a new `ItemCategoryDirectOrderDto` with the specified
	 * values.
	 *
	 * @param id                 The unique identifier of the item category.
	 * @param nameDefaultLang    The default language name of the item category.
	 * @param namePreferLang     The preferred language name of the item category.
	 * @param nameSupportiveLang The supportive language name of the item category.
	 */
	public RawMaterialCategoryDirectOrderDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang) {
		this.setId(id);
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
	}

}