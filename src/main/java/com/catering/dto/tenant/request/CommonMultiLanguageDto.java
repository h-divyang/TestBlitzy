package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CommonMultiLanguageDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 100, message = MessagesConstant.VALIDATION_NAME_MAX_100_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_2_LENGTH)
	public String nameDefaultLang;

	@Size(max = 100, message = MessagesConstant.VALIDATION_NAME_MAX_100_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_2_LENGTH)
	public String namePreferLang;

	@Size(max = 100, message = MessagesConstant.VALIDATION_NAME_MAX_100_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_2_LENGTH)
	public String nameSupportiveLang;

}