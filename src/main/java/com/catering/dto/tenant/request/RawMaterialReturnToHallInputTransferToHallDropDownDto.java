package com.catering.dto.tenant.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialReturnToHallInputTransferToHallDropDownDto extends CommonDataForDropDownDto {

	private Long hallId;

	private LocalDate transferDate;

	public RawMaterialReturnToHallInputTransferToHallDropDownDto(Long id, String nameDefaultLang, String namePreferLang,
			String nameSupportiveLang, Long hallId, LocalDate transferDate) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.hallId = hallId;
		this.transferDate = transferDate;
	}

}