package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package Menu Item category request for data save or update.
 * This DTO is used for transferring data related to Custom Package Menu Item category request for data save or update.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
public class SaveCustomPackageRecordMenuItemCategoryRequestDto extends IdDto {

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_MENU_ITEM_CATEGORY_NOT_NULL)
	private Long menuItemCategoryId;

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_NO_OF_ITEMS_NOT_NULL)
	private Long numberOfItems;

}