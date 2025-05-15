package com.catering.dto.tenant.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a user request.
 * This class extends 'UserDto' and includes additional information specific to user registration or update.
 * It provides getter and setter methods for accessing and modifying user request data.
 * A no-args constructor is available for creating instances without explicit initialization.
 * <br>
 * This class represents a lightweight object for transferring user request data between different
 * layers of the application, especially for user registration or update operations. It should not
 * contain complex business logic, which should be handled in service and domain layers instead.
 * <br>
 * The data validation for fields is performed using the provided validation annotations. Ensure
 * that user input is validated before creating or updating instances of this class.
 * <br>
 * Note: This class is part of the application's internal model and is not meant to be exposed
 *       to external clients directly. Avoid exposing this class through public APIs.
 * 
 * Field Validation Annotations:
 * - 'password': Must not be blank and must meet size constraints.
 * 
 * @see UserDto
 * @author krushali talaviya
 * @since July 2023
 */
@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto extends UserDto {

	/**
	 * The password for user registration or update.
	 * It must not be blank and should be between 8 and 15 characters in length.
	 * 
	 * @NotBlank(message = MessagesConstant.VALIDATION_PASSWORD_NOT_BLANK)
	 * @Size(max = 15, message = MessagesConstant.VALIDATION_PASSWORD_MAX_LENGTH)
	 * @Size(min = 8, message = MessagesConstant.VALIDATION_PASSWORD_MIN_LENGTH)
	 */
	@NotBlank(message = MessagesConstant.VALIDATION_PASSWORD_NOT_BLANK)
	@Size(max = 15, message = MessagesConstant.VALIDATION_PASSWORD_MAX_LENGTH)
	@Size(min = 8, message = MessagesConstant.VALIDATION_PASSWORD_MIN_LENGTH)
	private String password;

}