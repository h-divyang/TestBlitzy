package com.catering.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.catering.bean.Paging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseContainerDto<T> implements Serializable {

	private static final long serialVersionUID = -7791217884470823543L;
	private String message;
	private Integer status;
	private Boolean isError;
	private transient T body;
	private transient Paging paging;

	public ResponseContainerDto(T object) {
		this.message = "Success";
		this.isError = Boolean.FALSE;
		this.status = HttpStatus.OK.value();
		this.body = object;
	}

	public ResponseContainerDto(T object, String message, HttpStatus status) {
		this.message = message;
		this.isError = Boolean.TRUE;
		this.status = status.value();
		this.body = object;
	}

	public ResponseContainerDto<T> setSuccessMessage(String message) {
		this.message = message;
		this.isError = Boolean.FALSE;
		this.status = HttpStatus.OK.value();
		return this;
	}

	public ResponseContainerDto<T> setErrorMessage(String message, HttpStatus status) {
		this.message = message;
		this.isError = Boolean.TRUE;
		this.status = status.value();
		return this;
	}

	public ResponseContainerDto<T> setCustomPaging(Paging paging) {
		this.paging = paging;
		return this;
	}

}