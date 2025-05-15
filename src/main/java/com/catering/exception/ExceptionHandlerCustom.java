package com.catering.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.service.common.MessageService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.StringUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Provide Global Exception Handling for entire Application
 * */
@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExceptionHandlerCustom {

	Logger logger = LoggerFactory.getLogger(ExceptionHandlerCustom.class);

	MessageService messageService;

	/**
	 * Catch all the {@link BindException} which is occurred in the DTO validation
	 * */
	@ExceptionHandler(value = { BindException.class })
	public ResponseContainerDto<Map<String, String>> handleMethodArgumentNotValidException(BindException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String fieldName = error.getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		return getResponse(errors, messageService.getMessage(MessagesConstant.VALIDATION_MANDATORY_FIELD_NOT_BLANK), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Catch all the {@link HttpMediaTypeNotSupportedException}, It will occur when user trying to access APIs without proper Input or Request Body
	 * */
	@ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
	public <T> ResponseContainerDto<T> handleHttpMediaTypeNotSupportedException() {
		return getResponse(null, messageService.getMessage(MessagesConstant.REST_REQUEST_UNSUPPORTED_MEDIA_TYPE), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	/**
	 * Catch all the {@link HttpRequestMethodNotSupportedException}, It will occur when user trying to access APIs with different methods which is not available in the Application
	 * */
	@ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
	public <T> ResponseContainerDto<T> handleHttpRequestMethodNotSupportedExceptionn() {
		return getResponse(null, messageService.getMessage(MessagesConstant.REST_REQUEST_METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * Catch all the {@link HttpMessageNotReadableException}, It will occur when some of the field is not readable properly like Data, Numbers in the DTO
	 * */
	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	public ResponseContainerDto<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		if (ex.getRootCause() instanceof InvalidFormatException || ex.getRootCause() instanceof MismatchedInputException) {
			JsonMappingException jsonMappingException = (JsonMappingException) ex.getRootCause();
			if (Objects.nonNull(jsonMappingException)) {
				return createCommonException(jsonMappingException.getPath().get(jsonMappingException.getPath().size() - 1).getFieldName());
			}
		}
		if (ex.getRootCause() instanceof MismatchedInputException mismatchedInputException) {
			return createCommonException(mismatchedInputException.getPath().get(mismatchedInputException.getPath().size() - 1).getFieldName());
		}
		return getResponse(null, Objects.nonNull(ex.getRootCause()) && org.apache.commons.lang3.StringUtils.isNotBlank(ex.getRootCause().getMessage()) ? ex.getRootCause().getMessage() : messageService.getMessage(MessagesConstant.REST_REQUEST_REQUEST_BODY_MISSING), HttpStatus.BAD_REQUEST);
	}

	private ResponseContainerDto<Map<String, String>> createCommonException(String fieldName) {
		Map<String, String> errors = new HashMap<>();
		errors.put(fieldName, messageService.getMessage(MessagesConstant.VALIDATION_INVALID_FIELD, StringUtils.splitCamelCase(fieldName)));
		return getResponse(errors, messageService.getMessage(MessagesConstant.VALIDATION_MANDATORY_FIELD_NOT_BLANK), HttpStatus.BAD_REQUEST);
	}

	/**
	 * The user trying to log in with the wrong credentials
	 * */
	@ExceptionHandler(value = { BadCredentialsException.class })
	public <T> ResponseContainerDto<T> handleBadCredentialsException() {
		return getResponse(null, messageService.getMessage(MessagesConstant.JWT_BAD_CREDENTIALS), HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Catch all the {@link RestException} with response message and {@link HttpStatus}
	 * */
	@ExceptionHandler(value = { RestException.class })
	public ResponseContainerDto<Object> handleRestException(RestException ex) {
		return getResponse(ex.getBody(), ex.getMessage(), ex.getStatus());
	}

	/**
	 * Handles generic exceptions by logging the error and returning a standard error response.
	 * 
	 * @param ex the exception that was thrown
	 * @param <T> the type of response body
	 * @return a {@link ResponseContainerDto} containing the error message and HTTP status
	 */
	@ExceptionHandler(value = { Exception.class })
	public <T> ResponseContainerDto<T> handleException(Exception ex) {
		logger.error(ex.getMessage(), ex);
		return getResponse(null, messageService.getMessage(MessagesConstant.CORE_SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Helper method to generate a {@link ResponseContainerDto} with a provided body, message, and status.
	 * 
	 * @param body the body to include in the response (can be null)
	 * @param msg the message to include in the response
	 * @param status the HTTP status to return in the response
	 * @param <T> the type of the response body
	 * @return a {@link ResponseContainerDto} containing the provided body, message, and status
	 */
	private <T> ResponseContainerDto<T> getResponse(T body, String msg, HttpStatus status) {
		return RequestResponseUtils.generateResponseDto(body, msg, status);
	}

	/**
	 * Handles client abort exceptions (when a client aborts the request before it is completed).
	 * 
	 * @param ex the {@link ClientAbortException} that was thrown
	 */
	@ExceptionHandler(value = { ClientAbortException.class })
	public void handleClientAbortException(ClientAbortException ex) {
		logger.info("Client has aborted the request");
	}

}