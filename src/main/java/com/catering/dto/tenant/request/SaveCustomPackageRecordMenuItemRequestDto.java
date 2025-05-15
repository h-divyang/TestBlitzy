package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package Menu Item request for data save or update.
 * This DTO is used for transferring data related to Custom Package Menu Item request for data save or update.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
public class SaveCustomPackageRecordMenuItemRequestDto extends IdDto {

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_MENU_ITEM_NOT_NULL)
	private Long menuItemId;

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_MENU_ITEM_CATEGORY_NOT_NULL)
	private Long menuItemCategoryId;

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_MENU_ITEM_CATEGORY_SEQUENCE_NOT_NULL)
	private Long menuItemCategorySequence;

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_MENU_ITEM_SEQUENCE_NOT_NULL)
	private Long menuItemSequence;

}