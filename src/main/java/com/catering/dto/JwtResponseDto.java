package com.catering.dto;

import com.catering.dto.audit.OnlyIdDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JwtResponseDto extends OnlyIdDto {

	private String token;

	private String firstNameDefaultLang;

	private String firstNamePreferLang;

	private String firstNameSupportiveLang;

	private String lastNameDefaultLang;

	private String lastNamePreferLang;

	private String lastNameSupportiveLang;

	private String uniqueCode;

	private String avtar;

}