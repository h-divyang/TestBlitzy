package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.IdDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OrderNoItemsDto extends IdDto {

	@JsonBackReference
	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_REQUIRED)
	private OrderMenuPreparationDto menuPreparation;

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_NOT_BLANK)
	private RawMaterialDto rawMaterial;

}