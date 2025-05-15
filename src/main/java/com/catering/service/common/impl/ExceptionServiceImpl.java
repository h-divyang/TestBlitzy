package com.catering.service.common.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link ExceptionService} interface, providing methods for throwing
 * custom exceptions with specified HTTP status codes, messages, and optional body data.
 * This service utilizes the {@link MessageService} to retrieve localized error messages.
 * It ensures consistent error handling and response generation throughout the application.
 *
 * This implementation supports:
 * - Throwing generic internal server error exceptions.
 * - Throwing exceptions with custom HTTP statuses and messages.
 * - Throwing bad request exceptions with or without additional body data.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExceptionServiceImpl implements ExceptionService {

	/**
	 * Provides functionality for handling messages and related operations.
	 */
	MessageService messageService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void throwInternalServerErrorRestException() throws RestException {
		throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, messageService.getMessage(MessagesConstant.CORE_SOMETHING_WENT_WRONG), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void throwRestException(HttpStatus status, String message) throws RestException {
		throw new RestException(status, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void throwBadRequestException(String message) throws RestException {
		throwRestException(HttpStatus.BAD_REQUEST, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void throwRestException(HttpStatus status, String message, Object o) throws RestException {
		throw new RestException(status, message, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void throwBadRequestException(String message, Object o) throws RestException {
		throwRestException(HttpStatus.BAD_REQUEST, message, o);
	}

}