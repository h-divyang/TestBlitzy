package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AboutUsDto extends AuditIdDto {

	/**
	 * About us in the default language.
	 */
	private String aboutUsDefaultLang;

	/**
	 * About us in the preferred language.
	 */
	private String aboutUsPreferLang;

	/**
	 * About us in supportive language.
	 */
	private String aboutUsSupportiveLang;

}