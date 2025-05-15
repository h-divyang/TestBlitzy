package com.catering.model.tenant;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.bean.User;
import com.catering.model.audit.AuditIdModel;

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
public class CompanyUserModelForAudit extends AuditIdModel {

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

	/**
	 * Creates an instance of {@link CompanyUserModelForAudit} from a given {@link User} object.
	 * Copies relevant properties from the {@link User} object to the new instance.
	 * If the provided {@link User} object is null, an empty instance of {@link CompanyUserModelForAudit} is returned.
	 *
	 * @param user The {@link User} object containing the data to copy.
	 * @return A populated {@link CompanyUserModelForAudit} instance or an empty instance if the user is null.
	 */
	public static CompanyUserModelForAudit getInstanceOf(User user) {
		CompanyUserModelForAudit companyUser = CompanyUserModelForAudit.builder().build();
		if (Objects.nonNull(user)) {
			companyUser.setId(user.getUserId());
			companyUser.setFirstNameDefaultLang((user.getFirstNameDefaultLang()));
			companyUser.setFirstNamePreferLang(user.getFirstNamePreferLang());
			companyUser.setFirstNameSupportiveLang(user.getFirstNameSupportiveLang());
			companyUser.setLastNameDefaultLang(user.getLastNameDefaultLang());
			companyUser.setLastNamePreferLang(user.getLastNamePreferLang());
			companyUser.setLastNameSupportiveLang(user.getLastNameSupportiveLang());
		}
		return companyUser;
	}

}