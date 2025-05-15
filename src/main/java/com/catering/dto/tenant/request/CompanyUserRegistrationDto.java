package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.catering.annotation.Email;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CompanyUserRegistrationDto extends CompanyUserRegistrationRequestDto {

	@NotBlank(message = MessagesConstant.VALIDATION_USERNAME_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ALPHA_NUMERIC_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_USERNAME_NOT_VALID)
	@Size(max = 30, message = MessagesConstant.VALIDATION_USERNAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_USERNAME_MIN_LENGTH)
	private String username;

	@NotBlank(message = MessagesConstant.VALIDATION_PASSWORD_NOT_BLANK)
	@Size(max = 15, message = MessagesConstant.VALIDATION_PASSWORD_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String password;

	private LocalDateTime lastLogin;

	@NotBlank(message = MessagesConstant.VALIDATION_EMAIL_NOT_BLANK)
	@Email
	@Size(max = 70, message = MessagesConstant.VALIDATION_EMAIL_MAX_LENGTH)
	private String email;

	private String photo;

	// Use at registration time only
	@JsonIgnore
	private String tenant;

	@JsonIgnore
	private Long designationId;

	private String resetPasswordToken;

	@Valid
	@NotNull(message = MessagesConstant.REST_REQUEST_REQUEST_BODY_MISSING)
	private CompanyPreferencesRegistrationDto companyPreferences;

}