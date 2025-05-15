package com.catering.dto.tenant.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MenuItemRawMaterialDto extends AuditIdDto{

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_NOT_BLANK)
	private RawMaterialDto rawMaterial;

	@JsonBackReference
	@NotNull(message = MessagesConstant.VALIDATION_MENU_MATERIAL_NOT_BLANK)
	private MenuItemDto menuItem;

	@NotNull(message = MessagesConstant.VALIDATION_MEASUREMENT_NOT_BLANK)
	private MeasurementDto measurement;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double weight;

}