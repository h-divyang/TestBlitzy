package com.catering.dto;

import com.catering.constant.SwaggerConstant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = SwaggerConstant.AUTHENTICATION)
public class LoginRequestDto {

	private String username;

	private String password;

}