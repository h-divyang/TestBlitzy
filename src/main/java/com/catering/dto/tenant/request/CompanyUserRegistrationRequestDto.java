package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CompanyUserRegistrationRequestDto extends CompanyUserCommonDto {

	@NotBlank(message = FieldConstants.CONTACT_FIELD_MOBILE_NUMBER + MessagesConstant.VALIDATION_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 17, message = MessagesConstant.VALIDATION_MOBILE_MAX)
	@Size(min = 10, message = MessagesConstant.VALIDATION_MOBILE_MIN)
	private String mobileNumber;

}