package com.catering.model.tenant;

import java.time.LocalDateTime;
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
@Table(name = "company_user")
public class CompanyUserModel extends AuditByIdModel {

	@Column(name = "first_name_default_lang")
	private String firstNameDefaultLang;

	@Column(name = "first_name_prefer_lang")
	private String firstNamePreferLang;

	@Column(name = "first_name_supportive_lang")
	private String firstNameSupportiveLang;

	@Column(name = "last_name_default_lang")
	private String lastNameDefaultLang;

	@Column(name = "last_name_prefer_lang")
	private String lastNamePreferLang;

	@Column(name = "last_name_supportive_lang")
	private String lastNameSupportiveLang;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "reports_to")
	private Long reportsTo;

	@Column(name = "designation_id")
	private Long designationId;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "last_login_time")
	private LocalDateTime lastLogin;

	@Column(name = "permitted_ip")
	private String permittedIP;

	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	public static CompanyUserModel getInstance() {
		return new CompanyUserModel();
	}

	/**
	 * Creates a {@link CompanyUserModel} instance with all fields initialized to the same query value.
	 * This method is typically used for searching or filtering by a query string.
	 *
	 * @param query The query string to initialize the fields.
	 * @return A {@link CompanyUserModel} instance with fields set to the query value.
	 */
	public static CompanyUserModel ofSearchingModel(String query) {
		return CompanyUserModel.builder()
			.firstNameDefaultLang(query)
			.firstNameDefaultLang(query)
			.firstNameDefaultLang(query)
			.lastNameDefaultLang(query)
			.lastNamePreferLang(query)
			.lastNameSupportiveLang(query)
			.username(query)
			.build();
	}

}