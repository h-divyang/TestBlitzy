package com.catering.dto.tenant.request;

import com.catering.dto.audit.IdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Menu Item used at Custom package Menu Item record.
 * This DTO is used for transferring data related to Menu Item for record page.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageRecordMenuItemMasterResponseDto extends IdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}