package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseReportDropDownCommonDto extends OnlyIdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	public DateWiseReportDropDownCommonDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang) {
		super(id);
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
	}

}