package com.catering.controller.tenant;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.LoginLogDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.LoginLogService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing login logs.
 * 
 * This controller exposes a RESTful endpoint for retrieving login logs based on the provided
 * filter criteria. The endpoint ensures that only authorized users can access the logs, 
 * and it verifies the presence of a valid user ID in the request before proceeding.
 * 
 * The controller interacts with the `LoginLogService` to fetch login log data and handles
 * error scenarios through the `ExceptionService`. It also utilizes `MessageService` for 
 * retrieving messages for errors and validation.
 * 
 * @see LoginLogService for the service layer handling business logic related to login logs.
 * @see MessageService for fetching the messages related to error handling.
 * @see ExceptionService for managing exceptions and throwing appropriate errors.
 */
@RestController
@RequestMapping(value = ApiPathConstant.LOGIN_LOG)
@Tag(name = SwaggerConstant.LOGIN_LOG)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginLogController {

	/**
	 * Service for managing login log operations.
	 * This service is responsible for fetching, filtering, and processing login log data.
	 * It interacts with the repository layer to retrieve and store login log entries.
	 */
	LoginLogService  loginLogService;

	/**
	 * Service for managing messages.
	 * This service retrieves messages from a resource file or database based on keys.
	 * It is used for returning error or success messages in various parts of the application.
	 */
	MessageService messageService;

	/**
	 * Service for managing exceptions and error handling.
	 * This service is responsible for throwing appropriate exceptions with meaningful messages.
	 * It is used for validating inputs and handling error scenarios in the controller.
	 */
	ExceptionService exceptionService;

	/**
	 * Retrieves login logs for a specific user based on the provided filter criteria.
	 * 
	 * The method first checks if the user ID is present in the request. If not, it throws a 
	 * bad request exception. Then, it calls the `LoginLogService` to fetch the logs for the user.
	 * 
	 * @param filterDto The filter criteria for retrieving the login logs.
	 * @param request The HTTP request containing the user ID.
	 * @return A container DTO with a list of login logs.
	 * @throws RestException If the user ID is invalid or an error occurs while retrieving the logs.
	 */
	@GetMapping
	public ResponseContainerDto<List<LoginLogDto>> read(FilterDto filterDto, HttpServletRequest request) throws RestException {
		Object userId = request.getAttribute(Constants.USER_ID);
		if (Objects.isNull(userId)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		return loginLogService.read((Long) userId, filterDto);
	}

}