package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ContactCustomDto extends AuditIdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}