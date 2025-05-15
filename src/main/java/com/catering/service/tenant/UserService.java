package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RoleDto;
import com.catering.dto.tenant.request.UserDto;
import com.catering.dto.tenant.request.UserRequestDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing user-related operations.
 * This interface extends the 'GenericService' interface with 'UserDto', 'CompanyUserModel', and 'Long' as generic types.
 * It defines methods for creating, reading, and updating user data, as well as retrieving roles and active users.
 *
 * The 'UserService' is responsible for handling user-related business logic and coordinating with the data access layer.
 * Implementations of this interface will provide the concrete implementations of the defined methods.
 *
 * Note: This interface is intended to be implemented by service classes to provide the application's business logic
 *		 for user-related operations. The 'GenericService' provides common CRUD (Create, Read, Update, Delete) methods.
 *		 The 'UserDto' represents the data transfer object for user-related information,
 *		 and 'CompanyUserModel' represents the corresponding model/entity for user data in the database.
 *		 The 'Long' represents the type of the unique identifier used for user entities.
 *
 * @see GenericService
 * @see UserDto
 * @see CompanyUserModel
 * @see Long
 * @see UserRequestDto
 * @see ResponseContainerDto
 * @author krushali talaviya
 * @since July 2023
 */
public interface UserService extends GenericService<UserDto, CompanyUserModel, Long> {

	/**
	 * Method to create a user based on the provided 'userRequestDto'.
	 *
	 * This method receives a 'UserRequestDto' object representing user data to be created.
	 * The method returns an 'Optional' container holding the created user data as 'UserRequestDto'.
	 *
	 * @param userRequestDto The user data to be created.
	 * @param img An optional MultipartFile representing the new profile image. Pass null if you do not want to update the profile image.
	 * @return An 'Optional' container holding the created user data, or an empty container if the operation failed.
	 */
	Optional<UserRequestDto> create(UserRequestDto userRequestDto,  MultipartFile img);

	/**
	 * Method to update a user based on the provided 'userRequestDto'.
	 *
	 * This method receives a 'UserRequestDto' object representing user data to be updated.
	 * The method returns an 'Optional' container holding the updated user data as 'UserRequestDto'.
	 *
	 * @param userRequestDto The user data to be updated.
	 * @param img An optional MultipartFile representing the new profile image. Pass null if you do not want to update the profile image.
	 * @return An 'Optional' container holding the updated user data, or an empty container if the operation failed.
	 * @see UserRequestDto
	 * @see Optional
	 */
	UserDto update(UserDto userDto, MultipartFile img);
	/**
	 * Method to read a list of users based on the provided 'filterDto'.
	 *
	 * This method retrieves a list of users based on the filtering criteria specified in the 'filterDto'.
	 * The response is wrapped in a 'ResponseContainerDto' to standardize the response format.
	 *
	 * @param filterDto The filter criteria for retrieving users (optional).
	 * @return A 'ResponseContainerDto' containing the list of user data and metadata.
	 */
	ResponseContainerDto<List<UserDto>> read(FilterDto filterDto, HttpServletRequest request);

	/**
	 * Method to read a list of roles available in the application.
	 *
	 * This method retrieves a list of roles from the application.
	 * The response is wrapped in a 'ResponseContainerDto' to standardize the response format.
	 *
	 * @return A 'ResponseContainerDto' containing the list of role data.
	 */
	ResponseContainerDto<List<RoleDto>> read();

	/**
	 * Method to read a list of active users.
	 *
	 * This method retrieves a list of users marked as active from the application.
	 * The list of active users is returned directly as a 'List' of 'UserDto'.
	 *
	 * @return A 'List' containing the data of active users.
	 */
	List<UserDto> readUserByIsActive();

	/**
	 * Deletes a file associated with a user's profile or image.
	 *
	 * @param id The unique identifier of the user or image.
	 * @param isImage A boolean flag indicating whether the file is associated with an image (true) or a user profile (false).
	 */
	void deleteById(Long id, Boolean isImage);

	/**
	 * Updates the status (active/inactive) of a user or entity.
	 *
	 * @param id The unique identifier of the user or entity to update.
	 * @param status The new status to set (true for active, false for inactive).
	 * @return An Optional containing a UserDto if the user or entity is found and updated; otherwise, an empty Optional.
	 * @throws RestException If there's an error during the update.
	 */
	Optional<UserDto> updateStatus(Long id, Boolean status) throws RestException;

}