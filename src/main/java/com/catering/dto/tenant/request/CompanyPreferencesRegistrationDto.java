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

@Setter
@Getter
@NoArgsConstructor
public class CompanyPreferencesRegistrationDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_LANGUAGE_DEFAULT_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_LANGUAGE_DEFAULT_NOT_VALID)
	private String defaultLang;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_LANGUAGE_PREFER_NOT_VALID)
	private String preferLang;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_LANGUAGE_SUPPORTIVE_NOT_VALID)
	private String supportiveLang;

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_DEFAULT_LANGUAGE_NOT_BLANK)
	@Size(max = 50, message = MessagesConstant.VALIDATION_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_LENGTH)
	private String nameDefaultLanguage;

	@Size(max = 50, message = MessagesConstant.VALIDATION_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_LENGTH)
	private String namePreferLanguage;

	@Size(max = 50, message = MessagesConstant.VALIDATION_NAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_LENGTH)
	private String nameSupportiveLanguage;

	private String email;

}