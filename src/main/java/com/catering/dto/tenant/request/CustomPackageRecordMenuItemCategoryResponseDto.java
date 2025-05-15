package com.catering.dto.tenant.request;

import com.catering.dto.audit.IdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Menu Item Category record of Custom Package.
 * This DTO is used for transferring data related to Custom package menu item category for record page.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageRecordMenuItemCategoryResponseDto extends IdDto {

	private long customPackageId;

	private long menuItemCategoryId;

	private Long numberOfItems;

}