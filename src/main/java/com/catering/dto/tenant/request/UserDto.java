package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a user's information.
 * This class extends 'CompanyUserCommonDto' and contains specific user-related attributes.
 * It provides getter and setter methods for accessing and modifying user data.
 * A no-args constructor is available for creating instances without explicit initialization.
 * 
 * This class represents a lightweight object for transferring user data between different layers
 * of the application. It should not contain complex business logic, which should be handled in
 * service and domain layers instead. The data validation for fields is performed using the
 * provided validation annotations, so ensure that user input is validated before creating or
 * updating instances of this class.
 * 
 * Note: This class is part of the application's internal model and is not meant to be exposed
 *       to external clients directly. Avoid exposing this class through public APIs.
 * 
 * @see CompanyUserCommonDto
 * @author krushali talaviya
 * @since July 2023
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto extends CompanyUserCommonDto {

	/**
	 * The username of the user.
	 * It must not be blank and should consist of alphanumeric characters only, without spaces.
	 * The length must be between 2 and 30 characters.
	 * 
	 * @NotBlank(message = MessagesConstant.VALIDATION_USERNAME_NOT_BLANK)
	 * @Pattern(regexp = RegexConstant.ALPHA_NUMERIC_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_USERNAME_NOT_VALID)
	 * @Size(max = 30, message = MessagesConstant.VALIDATION_USERNAME_MAX_LENGTH)
	 * @Size(min = 2, message = MessagesConstant.VALIDATION_USERNAME_MIN_LENGTH)
	 */
	@NotBlank(message = MessagesConstant.VALIDATION_USERNAME_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ALPHA_NUMERIC_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_USERNAME_NOT_VALID)
	@Size(max = 30, message = MessagesConstant.VALIDATION_USERNAME_MAX_LENGTH)
	@Size(min = 2, message = MessagesConstant.VALIDATION_USERNAME_MIN_LENGTH)
	private String username;

	/**
	 * The email address of the user.
	 * It should be in a valid email format and should not exceed 70 characters in length.
	 * 
	 * @Pattern(regexp = RegexConstant.EMAIL, message = MessagesConstant.VALIDATION_EMAIL_NOT_VALID)
	 * @Size(max = 70, message = MessagesConstant.VALIDATION_EMAIL_MAX_LENGTH)
	 */
	@Pattern(regexp = RegexConstant.EMAIL, message = MessagesConstant.VALIDATION_EMAIL_NOT_VALID)
	@Size(max = 70, message = MessagesConstant.VALIDATION_EMAIL_MAX_LENGTH)
	private String email;

	/**
	 * The mobile number of the user.
	 * It should contain only numeric characters without spaces.
	 * The length must be between 10 and 17 characters.
	 * 
	 * @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	 * @Size(min = 10, max = 17, message = MessagesConstant.VALIDATION_MOBILE_RANGE)
	 */
	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 17, message = MessagesConstant.VALIDATION_MOBILE_MAX)
	@Size(min = 10, message = MessagesConstant.VALIDATION_MOBILE_MIN)
	private String mobileNumber;

	/**
	 * The string representing the user's photo.
	 * 
	 * Note: The photo itself is not validated by this annotation.
	 */
	private String photo;

	/**
	 * The unique identifier of the user's designation.
	 * It should consist of only numeric characters without spaces.
	 * 
	 * @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesFieldConstants.USER_FIELD_DESIGNATION_ID + MessagesConstant.VALIDATION_IS_INVALID)
	 * @NotNull(message = MessagesConstant.VALIDATION_USER_DESIGNATION_NOT_BLANK)
	 */
	@NotNull(message = MessagesConstant.VALIDATION_USER_DESIGNATION_NOT_BLANK)
	private Long designationId;

	/**
	 * The unique identifier of the user's reporting manager.
	 * It should consist of only numeric characters without spaces.
	 * 
	 * Note: This field is optional and may be null if the user has no reporting manager.
	 * @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesFieldConstants.USER_REPORTS_TO + MessagesConstant.VALIDATION_IS_INVALID)
	 */
	private Long reportsTo;

	/**
	 * This variable represents the email verification status.
	 * If true, it means that the email has been verified; otherwise, it is false.
	 */
	private Boolean isEmailVerify = true;

	private String token;

}