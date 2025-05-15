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

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class CompanyBankDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_ACCOUNT_NAME_NOT_BLANK)
	@Size(max = 70, message = MessagesConstant.VALIDATION_ACCOUNT_NAME_MAX)
	@Size(min = 2, message = MessagesConstant.VALIDATION_ACCOUNT_NAME_MIN)
	private String accountName;

	@NotBlank(message = MessagesConstant.VALIDATION_ACCOUNT_NUMBER_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_ACCOUNT_NUMBER_NOT_VALID)
	@Size(max = 30, message = MessagesConstant.VALIDATION_ACCOUNT_NUMBER_MAX)
	@Size(min = 9, message = MessagesConstant.VALIDATION_ACCOUNT_NUMBER_MIN)
	private String accountNumber;

	@NotBlank(message = MessagesConstant.VALIDATION_BRANCH_NAME_NOT_BLANK)
	@Size(max = 30, message = MessagesConstant.VALIDATION_BRANCH_NAME_MAX)
	@Size(min = 2, message = MessagesConstant.VALIDATION_BRANCH_NAME_MIN)
	private String branchName;

	@NotBlank(message = MessagesConstant.VALIDATION_IFSC_CODE_NOT_BLANK)
	@Size(max = 11, message = MessagesConstant.VALIDATION_IFSC_MAX)
	@Size(min = 11, message = MessagesConstant.VALIDATION_IFSC_MIN)
	private String ifscCode;

	private String qrCode;

}