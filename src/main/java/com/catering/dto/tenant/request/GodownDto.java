package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GodownDto extends CommonMultiLanguageDto {

	@NotBlank(message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_NOT_BLANK)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String addressDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String addressPreferLang;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String addressSupportiveLang;

}