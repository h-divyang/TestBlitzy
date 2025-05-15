package com.catering.dto.tenant.request;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CommonMultiLanguageWithoutAuditDto extends IdDto {

	public String nameDefaultLang;

	public String namePreferLang;

	public String nameSupportiveLang;

}