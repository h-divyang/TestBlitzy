package com.catering.model.audit;

import java.util.Objects;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.catering.dto.audit.AuditDto;
import com.catering.dto.audit.AuditUserDto;
import com.catering.model.tenant.CompanyUserModelForAudit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditByIdModel extends AuditIdModel {

	@OneToOne
	@JoinColumn(name = "created_by", updatable = false)
	private CompanyUserModelForAudit createdBy;

	@OneToOne
	@JoinColumn(name = "updated_by")
	private CompanyUserModelForAudit updatedBy;

	public AuditDto getAudit() {
		return AuditDto.builder()
			.create(
					AuditUserDto.builder()
						.time(getCreatedAt())
						.firstNameDefaultLang(Objects.nonNull(createdBy) ? createdBy.getFirstNameDefaultLang() : null)
						.firstNamePreferLang(Objects.nonNull(createdBy) ? createdBy.getFirstNamePreferLang() : null)
						.firstNameSupportiveLang(Objects.nonNull(createdBy) ? createdBy.getFirstNameSupportiveLang() : null)
						.lastNameDefaultLang(Objects.nonNull(createdBy) ? createdBy.getLastNameDefaultLang() : null)
						.lastNamePreferLang(Objects.nonNull(createdBy) ? createdBy.getLastNamePreferLang() : null)
						.lastNameSupportiveLang(Objects.nonNull(createdBy) ? createdBy.getLastNameSupportiveLang() : null)
						.build())
			.update(AuditUserDto.builder()
					.time(getUpdatedAt())
					.firstNameDefaultLang(Objects.nonNull(updatedBy) ? updatedBy.getFirstNameDefaultLang() : null)
					.firstNamePreferLang(Objects.nonNull(updatedBy) ? updatedBy.getFirstNamePreferLang() : null)
					.firstNameSupportiveLang(Objects.nonNull(updatedBy) ? updatedBy.getFirstNameSupportiveLang() : null)
					.lastNameDefaultLang(Objects.nonNull(updatedBy) ? updatedBy.getLastNameDefaultLang() : null)
					.lastNamePreferLang(Objects.nonNull(updatedBy) ? updatedBy.getLastNamePreferLang() : null)
					.lastNameSupportiveLang(Objects.nonNull(updatedBy) ? updatedBy.getLastNameSupportiveLang() : null)
					.build())
			.editCount(getEditCount())
			.id(getId())
			.build();
	}

}