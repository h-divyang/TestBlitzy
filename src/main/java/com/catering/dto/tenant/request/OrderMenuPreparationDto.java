package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OrderMenuPreparationDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_FUNCTION_REQUIRED)
	private OrderFunctionDto orderFunction;

	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_MENU_TYPE_REQUIRED)
	private Long menuTypeId;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_RATE_REQUIRED)
	private Float rate;

	@Valid
	@JsonManagedReference
	private List<OrderMenuPreparationMenuItemDto> menuPreparationMenuItem;

	@Valid
	private List<OrderMenuPreparationMenuItemCategoryDto> menuPreparationMenuItemCategory;

	@Valid
	@JsonManagedReference
	private List<OrderNoItemsDto> noItems;

	private NameDto customPackage;

}