package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AdminReportNamePlateDto extends OnlyIdDto {

	private Integer repeatNumber;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private String counterPlateMenuItemNameDefaultLang;

	private String counterPlateMenuItemNamePreferLang;

	private String counterPlateMenuItemNameSupportiveLang;

	public AdminReportNamePlateDto(Long id, String nameDefaultLang,
			String namePreferLang, String nameSupportiveLang, String counterPlateMenuItemNameDefaultLang, String counterPlateMenuItemNamePreferLang, String counterPlateMenuItemNameSupportiveLang, Integer repeatNumber) {
		this.setId(id);
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
		this.counterPlateMenuItemNameDefaultLang = counterPlateMenuItemNameDefaultLang;
		this.counterPlateMenuItemNamePreferLang = counterPlateMenuItemNamePreferLang;
		this.counterPlateMenuItemNameSupportiveLang = counterPlateMenuItemNameSupportiveLang;
		this.repeatNumber = repeatNumber;
	}

}