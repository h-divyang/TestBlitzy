package com.catering.dto.tenant.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.audit.AuditIdDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtraExpenseDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	@JsonBackReference
	private OrderFunctionDto orderFunction;

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 50, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameDefaultLang;

	@Size(max = 50, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String namePreferLang;

	@Size(max = 50, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameSupportiveLang;

	@Digits(integer = 5, fraction = 0, message = MessagesConstant.VALIDATION_PAX_INVALID)
	private Integer qty;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double price;

}