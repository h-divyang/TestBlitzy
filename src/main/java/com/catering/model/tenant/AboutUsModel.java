package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "about_us")
public class AboutUsModel extends AuditByIdModel {

	/**
	 * About us in the default language.
	 */
	@Column(name = "about_us_default_lang")
	private String aboutUsDefaultLang;

	/**
	 * About us in the preferred language.
	 */
	@Column(name = "about_us_prefer_lang")
	private String aboutUsPreferLang;

	/**
	 * About us in supportive language.
	 */
	@Column(name = "about_us_supportive_lang")
	private String aboutUsSupportiveLang;

}