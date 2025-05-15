package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object (DTO) for representing a purchase order contact with GST number.
 * Extends CommonDataForDropDownDto.
 *
 * @author Krushali Talaviya
 * @since 2024-06-01
 */
@Getter
@Setter
public class PurchaseOrderContactDto extends CommonDataForDropDownDto {

	private String gstNumber;

	/**
	 * Constructor for PurchaseOrderContactDto.
	 *
	 * @param id                 The ID of the purchase order contact
	 * @param nameDefaultLang    The name in the default language
	 * @param namePreferLang     The name in the preferred language
	 * @param nameSupportiveLang The name in a supportive language
	 * @param gstNumber          The GST number of the purchase order contact
	 */
	public PurchaseOrderContactDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, String gstNumber) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.gstNumber = gstNumber;
	}

}