package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

	private Long id;

	@NotBlank(message = MessagesConstant.VALIDATION_PASSWORD_NOT_BLANK)
	@Size(max = 30, message = MessagesConstant.VALIDATION_PASSWORD_MAX_LENGTH)
	@Size(min = 8, message = MessagesConstant.VALIDATION_PASSWORD_MIN_LENGTH)
	private String password;

	@NotBlank(message = MessagesConstant.VALIDATION_PASSWORD_NOT_BLANK)
	@Size(max = 30, message = MessagesConstant.VALIDATION_PASSWORD_MAX_LENGTH)
	@Size(min = 8, message = MessagesConstant.VALIDATION_PASSWORD_MIN_LENGTH)
	private String newPassword;

}