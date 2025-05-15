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
public class AdminReportTableMenuDto extends OnlyIdDto {

	private Integer repeatNumber;

	private Integer isMenuItemSelected;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private String counterPlateMenuItemNameDefaultLang;

	private String counterPlateMenuItemNamePreferLang;

	private String counterPlateMenuItemNameSupportiveLang;

	public AdminReportTableMenuDto(Long id, String nameDefaultLang,
			String namePreferLang, String nameSupportiveLang, String counterPlateMenuItemNameDefaultLang, String counterPlateMenuItemNamePreferLang, String counterPlateMenuItemNameSupportiveLang, Integer repeatNumber, Integer isMenuItemSelected) {
		this.setId(id);
		this.nameDefaultLang = nameDefaultLang;
		this.namePreferLang = namePreferLang;
		this.nameSupportiveLang = nameSupportiveLang;
		this.counterPlateMenuItemNameDefaultLang = counterPlateMenuItemNameDefaultLang;
		this.counterPlateMenuItemNamePreferLang = counterPlateMenuItemNamePreferLang;
		this.counterPlateMenuItemNameSupportiveLang = counterPlateMenuItemNameSupportiveLang;
		this.repeatNumber = repeatNumber;
		this.isMenuItemSelected = isMenuItemSelected;
	}

}