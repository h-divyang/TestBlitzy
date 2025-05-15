package com.catering.controller.tenant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RoleDto;
import com.catering.dto.tenant.request.UserDto;
import com.catering.dto.tenant.request.UserRequestDto;
import com.catering.enums.RoleEnum;
import com.catering.exception.RestException;
import com.catering.properties.FileProperties;
import com.catering.properties.JwtProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.UserService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing user-related operations.
 * This class handles HTTP requests related to users, such as creating, reading, and updating user data.
 * The endpoints are mapped under the base path defined by {@link ApiPathConstant#USER}.
 * 
 * This controller uses the Spring Web annotation '@RestController', making it a specialized
 * version of the '@Controller' annotation. It automatically serializes and deserializes the
 * response and request bodies as JSON. Additionally, the '@RequestMapping' annotation is used
 * to specify the base request mapping for all the endpoints within this controller.
 * 
 * Swagger API documentation for this controller is provided using the '@Tag' annotation,
 * which groups the endpoints under the tag defined by {@link SwaggerConstant#USER}.
 * 
 * Note: This controller is responsible for handling user-related requests and delegating
 *       the actual business logic to the 'UserService' class. It also uses the 'MessageService'
 *       class to retrieve localized messages for the response.
 * 
 * @see UserService
 * @see MessageService
 * @see UserRequestDto
 * @see UserDto
 * @see RoleDto
 * @see FilterDto
 * @see ResponseContainerDto
 * @see RequestResponseUtils
 * @author krushali talaviya
 * @since July 2023
 */
@RestController
@RequestMapping(value = ApiPathConstant.USER)
@Tag(name = SwaggerConstant.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

	/**
	 * Service for managing user-related operations.
	 */
	UserService userService;

	/** 
	 * Service for handling messaging operations.
	 */
	MessageService messageService;

	/**
	 * Service for generating and validating JWT tokens.
	 */
	JwtService jwtService;

	/**
	 * Configuration properties for JWT token management.
	 */
	JwtProperties jwtProperties;

	/**
	 * Configuration properties for file-related operations.
	 */
	FileProperties fileProperties;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Endpoint to create a new user.
	 * 
	 * This method receives a 'UserRequestDto' object in the request body, which contains
	 * the user's information to be created. The '@Valid' annotation ensures that the
	 * received data is validated against the constraints specified in the 'UserRequestDto' class.
	 * The created user response is wrapped in an 'Optional' container for nullable results.
	 * The response message is retrieved from 'MessageService' using the constant message key.
	 * 
	 * @param userRequestDto The user data for creating a new user.
	 * @return A 'ResponseContainerDto' containing the optional created user response and a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<UserRequestDto>> create(@Valid UserRequestDto userRequestDto, @RequestParam(name= Constants.IMAGE, required = false) MultipartFile image) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(image, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		Optional<UserRequestDto> userResponseDto = userService.create(userRequestDto, image);
		return RequestResponseUtils.generateResponseDto(userResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint to read a list of users based on filter criteria.
	 * 
	 * This method retrieves a list of users based on the provided 'filterDto'.
	 * The 'filterDto' contains filtering criteria to narrow down the search results.
	 * The response is wrapped in a 'ResponseContainerDto' to standardize the response format.
	 * 
	 * @param filterDto The filter criteria for retrieving users (optional).
	 * @return A 'ResponseContainerDto' containing the list of user data and metadata.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<UserDto>> read(FilterDto filterDto, HttpServletRequest request) {
		return userService.read(filterDto, request);
	}

	/**
	 * Endpoint to update an existing user's information.
	 * 
	 * This method receives a 'UserRequestDto' object in the request body, which contains
	 * the user's updated information. The '@Valid' annotation ensures that the received data
	 * is validated against the constraints specified in the 'UserRequestDto' class.
	 * The updated user response is wrapped in an 'Optional' container for nullable results.
	 * The response message is retrieved from 'MessageService' using the constant message key.
	 * 
	 * @param userRequestDto The user data for updating an existing user.
	 * @return A 'ResponseContainerDto' containing the optional updated user response and a success message.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<UserDto> update(@Valid UserDto userDto, @RequestParam(name= Constants.IMAGE, required = false) MultipartFile image, HttpServletRequest request) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(image, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		UserDto userDtoResponse = userService.update(userDto, image);
		Object tokenObject = request.getAttribute(Constants.TOKEN);
		if (Objects.nonNull(tokenObject) && !Objects.equals(userDto.getUsername(), jwtService.extractUsername(tokenObject.toString())) && Objects.equals(userDto.getId(), jwtService.extractUserId(tokenObject.toString()))) {
			Jws<Claims> jwsClaims = jwtService.extractAllClaims(tokenObject.toString());
			Claims claims = jwsClaims.getBody();
			claims.setSubject(userDto.getUsername());
			claims.replace(jwtProperties.getAuthorities(), new String[] {RoleEnum.ROLES.stream().filter(role -> role.getId().equals(userDto.getDesignationId())).map(RoleDto::getRole).findFirst().orElse(null)});
			String token = jwtService.createToken(claims);
			userDtoResponse.setToken(token);
		}
		return RequestResponseUtils.generateResponseDto(userDtoResponse, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to read a list of roles available in the application.
	 * 
	 * This method retrieves a list of roles from the 'userService'.
	 * The response is wrapped in a 'ResponseContainerDto' to standardize the response format.
	 * 
	 * @return A 'ResponseContainerDto' containing the list of role data.
	 */
	@GetMapping(value = ApiPathConstant.ROLES)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<RoleDto>> read() {
		return userService.read();
	}

	/**
	 * Endpoint to read a list of active users.
	 * 
	 * This method retrieves a list of users marked as active from the 'UserService'.
	 * The response is wrapped in a 'ResponseContainerDto' to standardize the response format.
	 * 
	 * @return A 'ResponseContainerDto' containing the list of active user data.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<UserDto>> readUserByIsActive() {
		return RequestResponseUtils.generateResponseDto(userService.readUserByIsActive());
	}

	/**
	 * Deletes a user or image based on the provided ID and isImage flag.
	 *
	 * @param idStr    The unique identifier of the user or image as a string.
	 * @param isImage  A boolean flag indicating whether the resource is an image (true) or a user (false).
	 * @return A ResponseContainerDto containing the result of the deletion operation.
	 * @throws RestException if there's an error during the deletion.
	 */
	@DeleteMapping(value = ApiPathConstant.ID + ApiPathConstant.IS_IMAGE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr, @PathVariable(name = FieldConstants.IS_IMAGE) Boolean isImage) throws RestException {
		userService.deleteById(Long.valueOf(idStr), isImage);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Updates the status (active/inactive) of a user.
	 *
	 * @param userDto  The UserDto object containing the user's information and status.
	 * @return A ResponseContainerDto containing an Optional<UserDto> if the user is found and updated.
	 */
	@PatchMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<UserDto>> updateStatus(@Valid @RequestBody UserDto userDto) {
		return RequestResponseUtils.generateResponseDto(userService.updateStatus(userDto.getId(), userDto.getIsActive()), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}