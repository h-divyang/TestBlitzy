package com.catering.model.tenant;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company_preferences")
public class CompanyPreferencesModel extends AuditByIdModel {

	@Column(name = "default_lang", updatable = false)
	private Long defaultLang;

	@Column(name = "prefer_lang", updatable = false)
	private Long preferLang;

	@Column(name = "supportive_lang", updatable = false)
	private Long supportiveLang;

	@Column(name = "name_default_lang")
	private String nameDefaultLanguage;

	@Column(name = "name_prefer_lang")
	private String namePreferLanguage;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLanguage;

	@Column(name = "tagline")
	private String tagLine;

	@Column(name = "address_default_lang")
	private String addressDefaultLanguage;

	@Column(name = "address_prefer_lang")
	private String addressPreferLanguage;

	@Column(name = "address_supportive_lang")
	private String addressSupportiveLanguage;

	@Column(name = "email_id")
	private String email;

	@Column(name = "website")
	private String website;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "office_number")
	private String officeNumber;

	@Column(name = "gst_number")
	private String gst;

	@Column(name = "invoice_address_default_lang")
	private String invoiceAddressDefaultLanguage;

	@Column(name = "invoice_address_prefer_lang")
	private String invoiceAddressPreferLanguage;

	@Column(name = "invoice_address_supportive_lang")
	private String invoiceAddressSupportiveLanguage;

	@Column(name = "active_date", updatable = false)
	private LocalDate activeDate;

	@Column(name = "due_date", updatable = false)
	private LocalDate dueDate;

	@Column(name = "users", updatable = false)
	private Integer users;

	@Column(name = "extra_users", updatable = false)
	private Integer extraUsers;

	@Column(name = "fk_subscription_id", updatable = false)
	private Long subscriptionId;

	@Column(name = "subscription_type", insertable = false, updatable = false)
	private int subscriptionType;

	@Column(name = "food_licence_number")
	private String foodLicenceNumber;

}