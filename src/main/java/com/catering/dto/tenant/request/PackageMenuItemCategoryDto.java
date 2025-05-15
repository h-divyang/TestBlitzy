package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package Menu Item Category.
 * This DTO is used for transferring data related to Custom Package Menu Item Category.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PackageMenuItemCategoryDto extends OnlyIdDto {

	private MenuItemCategoryDto menuItemCategory;

	private Long numberOfItems;

}