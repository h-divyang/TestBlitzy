package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.dto.audit.AuditIdDto;
import com.catering.dto.superadmin.CommonDataDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ContactCategoryTypeDto extends AuditIdDto implements CommonDataDto {

	@Pattern(regexp = RegexConstant.ALPHA_NUMERIC_WITH_SPACE, message = MessagesConstant.VALIDATION_NAME_NOT_VALID_ALLOW_ONLY_ALPHA_NUMERIC)
	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}