package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CompanyUserCommonDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_FIRST_NAME_DEFAULT_NOT_BLANK)
	@Size(max = 50, message = MessagesConstant.FIRST_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.FIRST_NAME_MIN_LENGTH)
	private String firstNameDefaultLang;

	@Size(max = 50, message = MessagesConstant.FIRST_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.FIRST_NAME_MIN_LENGTH)
	private String firstNamePreferLang;

	@Size(max = 50, message = MessagesConstant.FIRST_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.FIRST_NAME_MIN_LENGTH)
	private String firstNameSupportiveLang;

	@NotBlank(message = MessagesConstant.VALIDATION_LAST_NAME_DEFAULT_NOT_BLANK)
	@Size(max = 50, message = MessagesConstant.LAST_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.LAST_NAME_MIN_LENGTH)
	private String lastNameDefaultLang;

	@Size(max = 50, message = MessagesConstant.LAST_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.LAST_NAME_MIN_LENGTH)
	private String lastNamePreferLang;

	@Size(max = 50, message = MessagesConstant.LAST_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.LAST_NAME_MIN_LENGTH)
	private String lastNameSupportiveLang;

	@Pattern(regexp = RegexConstant.PERMITTED_IP, message = MessagesConstant.VALIDATION_PERMITTED_IP)
	private String permittedIP;

}