package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralLedgerContactDropDownDto extends CommonDataForDropDownDto {

	private LocalDate lockDate;

	public GeneralLedgerContactDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, LocalDate lockDate) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.lockDate = lockDate;
	}

}