package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) representing terms and conditions.
 *
 * @extends AuditIdDto Base DTO with audit-related fields.
 *
 * @since 2023-12-18
 * @author Krushali Talaviya
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TermsAndConditionsDto extends AuditIdDto {

	/**
	 * Content in the default language.
	 */
	private String contentDefaultLang;

	/**
	 * Content in the preferred language.
	 */
	private String contentPreferLang;

	/**
	 * Content in supportive language.
	 */
	private String contentSupportiveLang;

}