package com.catering.controller.tenant;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.ChangePasswordDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationRequestDto;
import com.catering.exception.RestException;
import com.catering.properties.FileProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanyUserService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for handling company user-related actions such as reading data,
 * updating registration details, and changing passwords.
 */
@RestController
@RequestMapping(value = ApiPathConstant.COMPANY_USER)
@Tag(name = SwaggerConstant.COMPANY_USER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyUserController {

	/**
	 * Service for handling company user-related business logic.
	 */
	CompanyUserService companyUserService;

	/**
	 * Service for retrieving localized messages for responses and validation.
	 */
	MessageService messageService;

	/**
	 * Service for managing and throwing custom application exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Properties configuration for handling file-related settings.
	 */
	FileProperties fileProperties;

	/**
	 * Fetches the registration details of the company user associated with the given request context.
	 *
	 * @param request The {@link HttpServletRequest} from which the user ID is extracted to identify the company user
	 * @return A {@link ResponseContainerDto} containing an {@link Optional} that holds the
	 *		   {@link CompanyUserRegistrationDto} if found, or an empty Optional if not
	 * @throws RestException If any validation or request handling error occurs
	 */
	@GetMapping
	public ResponseContainerDto<Optional<CompanyUserRegistrationDto>> read(HttpServletRequest request) throws RestException {
		ErrorGenerator errors = ErrorGenerator.builder();
		Object userId = request.getAttribute(Constants.USER_ID);
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		return RequestResponseUtils.generateResponseDto(companyUserService.read((Long) userId));
	}

	/**
	 * Updates the registration details of a company user.
	 *
	 * @param companyUser A {@link CompanyUserRegistrationRequestDto} object containing the data to update for the company user.
	 * @param avtar An optional {@link MultipartFile} representing the avatar image for the user, subject to validation.
	 * @param request The {@link HttpServletRequest} containing additional context, such as the user ID.
	 * @return A {@link ResponseContainerDto} containing a {@link CompanyUserRegistrationDto} object, along with a success message.
	 * @throws RestException If validation issues or internal server errors occur during processing.
	 */
	@PutMapping
	public ResponseContainerDto<CompanyUserRegistrationDto> update(@Valid CompanyUserRegistrationRequestDto companyUser, @RequestParam(name= Constants.IMAGE, required = false) MultipartFile avtar, HttpServletRequest request) {
		ErrorGenerator errors = ErrorGenerator.builder();
		Object userId = request.getAttribute(Constants.USER_ID);
		errors.getErrors().putAll(FileUtils.imageValidate(avtar, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		companyUser.setId((Long) userId);
		if (companyUserService.update(companyUser, avtar) != 1) {
			exceptionService.throwInternalServerErrorRestException();
		}
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Handles requests to change the password for the authenticated company user.
	 *
	 * @param changePasswordDto The {@link ChangePasswordDto} object containing the current and new password details.
	 * @param request The {@link HttpServletRequest} object containing the user's session and context, used to retrieve the user ID.
	 * @return A {@link ResponseContainerDto} containing an {@link Optional} of {@link CompanyUserRegistrationDto},
	 *         which will be empty as there is no relevant data to return for a password change request.
	 * @throws RestException if an internal server error occurs during password change processing.
	 */
	@PutMapping(ApiPathConstant.CHANGE_PASSWORD)
	public ResponseContainerDto<Optional<CompanyUserRegistrationDto>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request) {
		Object userId = request.getAttribute(Constants.USER_ID);
		changePasswordDto.setId((Long) userId);
		if (companyUserService.changePassword(changePasswordDto) != 1) {
			exceptionService.throwInternalServerErrorRestException();
		}
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}