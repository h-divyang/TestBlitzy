package com.catering.dto.tenant.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MeasurementDto extends CommonMultiLanguageDto {

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 10, message = MessagesConstant.VALIDATION_SYMBOL_MAX_10_LENGTH)
	private String symbolDefaultLang;

	@Size(max = 10, message = MessagesConstant.VALIDATION_SYMBOL_MAX_10_LENGTH)
	private String symbolPreferLang;

	@Size(max = 10, message = MessagesConstant.VALIDATION_SYMBOL_MAX_10_LENGTH)
	private String symbolSupportiveLang;

	@NotNull(message = MessagesConstant.VALIDATION_MEASUREMENT_IS_BASE_UNIT_NOT_BLANK)
	private Boolean isBaseUnit;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double baseUnitEquivalent;

	private MeasurementDto baseUnitId;

	private Integer decimalLimitQty;

	private Integer adjustType;

	private Double stepWiseRange;

}