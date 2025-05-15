package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.IdDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MealTypeNoItemsDto extends IdDto {

	@JsonBackReference
	@NotNull(message = MessagesConstant.VALIDATION_MEAL_TYPE_REQUIRED)
	private MealTypeDto mealType;

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_NOT_BLANK)
	private RawMaterialDto rawMaterial;

}