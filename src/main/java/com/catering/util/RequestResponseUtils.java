package com.catering.util;

import org.springframework.http.HttpStatus;

import com.catering.dto.ResponseContainerDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Provide utilities to generate response DTO with the same structure and container
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestResponseUtils {

	/**
	 * Provide {@link ResponseContainerDto} of the T type of body with success message and 200 {@link HttpStatus} code
	 * 
	 * @param object T type 
	 * @return {@link ResponseContainerDto} of T type
	 * */
	public static <T> ResponseContainerDto<T> generateResponseDto(T object) {
		return new ResponseContainerDto<>(object);
	}

	/**
	 * Provide {@link ResponseContainerDto} of the T type of body with custom message and 200 {@link HttpStatus} code
	 * 
	 * @param object T type
	 * @param message
	 * 
	 * @return {@link ResponseContainerDto} of T type
	 * */
	public static <T> ResponseContainerDto<T> generateResponseDto(T o, String message) {
		return new ResponseContainerDto<T>(o).setSuccessMessage(message);
	}

	/**
	 * Provide {@link ResponseContainerDto} of the T type of body with custom message and custom {@link HttpStatus} code
	 * 
	 * @param object T type
	 * @param message
	 * @param status {@link HttpStatus} type of response code
	 * 
	 * @return {@link ResponseContainerDto} of T type
	 * */
	public static <T> ResponseContainerDto<T> generateResponseDto(T o, String message, HttpStatus status) {
		return new ResponseContainerDto<T>(o).setErrorMessage(message, status);
	}

}