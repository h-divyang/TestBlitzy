package com.catering.service.tenant;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.ChangePasswordDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationRequestDto;
import com.catering.exception.RestException;

/**
 * Interface defining the methods for managing company user-related operations
 * such as user registration updates, retrieving user information, and managing passwords.
 */
public interface CompanyUserService {

	/**
	 * Updates the company user registration information along with an avatar image.
	 *
	 * @param companyUserDto The DTO containing the updated company user registration details.
	 * @param avtar The profile image file to be updated for the user. Pass null if the avatar is not being updated.
	 * @return An integer indicating the result of the update operation.
	 * @throws RestException If an error occurs during the update operation.
	 */
	int update(CompanyUserRegistrationRequestDto companyUserDto, MultipartFile avtar) throws RestException;

	/**
	 * Retrieves a company user registration by their user name.
	 *
	 * @param username The user name of the company user to be retrieved.
	 * @return An Optional containing the CompanyUserRegistrationDto if the user is found; otherwise, an empty Optional.
	 */
	Optional<CompanyUserRegistrationDto> findByUsername(String username);

	/**
	 * Changes the password of a user.
	 *
	 * @param changePasswordDto The DTO containing the user's current password and new password details.
	 * @return An integer indicating the outcome of the password change operation, typically used as a status code.
	 */
	int changePassword(ChangePasswordDto changePasswordDto);

	/**
	 * Resets the user's password using a provided token and new password.
	 *
	 * @param token The token provided to authorize the password reset.
	 * @param newPassword The new password to set for the user.
	 * @return A ResponseContainerDto containing a success or error message, status, and additional details
	 *		   indicating the outcome of the password reset operation.
	 */
	ResponseContainerDto<String> resetPassword(String token, String newPassword);

	/**
	 * Retrieves a company user registration by its unique identifier.
	 *
	 * @param id The unique identifier of the company user to be retrieved.
	 * @return An Optional containing the CompanyUserRegistrationDto if the user is found; otherwise, an empty Optional.
	 */
	Optional<CompanyUserRegistrationDto> read(Long id);

}