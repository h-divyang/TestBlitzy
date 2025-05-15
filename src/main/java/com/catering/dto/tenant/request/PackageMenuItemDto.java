package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package Menu Item.
 * This DTO is used for transferring data related to Custom Package Menu Item.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PackageMenuItemDto extends OnlyIdDto {

	private MenuItemCategoryDto menuItemCategory;

	private MenuItemDto menuItem;

	private Long menuItemSequence;

	private Long menuItemCategorySequence;

}