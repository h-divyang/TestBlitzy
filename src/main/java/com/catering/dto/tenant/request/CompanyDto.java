package com.catering.dto.tenant.request;

import java.util.UUID;

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
public class CompanyDto extends AuditIdDto {

	private UUID tenant;

	@NotBlank(message = MessagesConstant.VALIDATION_UNIQUE_CODE_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ALPHA_NUMERIC_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_UNIQUE_CODE_NOT_VALID)
	@Size(max = 5, message = MessagesConstant.VALIDATON_UNIQUE_CODE_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATON_UNIQUE_CODE_MIN_LENGTH)
	private String uniqueCode;

}