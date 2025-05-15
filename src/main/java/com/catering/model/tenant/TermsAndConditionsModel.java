package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity model class representing terms and conditions.
 *
 * @extends AuditByIdModel Base entity model with audit-related fields and primary key.
 *
 * @since 2023-12-18
 * @author Krushali Talaviya
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "terms_and_conditions")
public class TermsAndConditionsModel extends AuditByIdModel {

	/**
	 * Content in the default language.
	 */
	@Column(name = "content_default_lang")
	private String contentDefaultLang;

	/**
	 * Content in the preferred language.
	 */
	@Column(name = "content_prefer_lang")
	private String contentPreferLang;

	/**
	 * Content in supportive language.
	 */
	@Column(name = "content_supportive_lang")
	private String contentSupportiveLang;

}