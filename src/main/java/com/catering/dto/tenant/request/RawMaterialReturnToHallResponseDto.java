package com.catering.dto.tenant.request;

import java.time.LocalDate;
import com.catering.dto.audit.OnlyIdDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialReturnToHallResponseDto extends OnlyIdDto {

	private LocalDate returnDate;

	private HallMasterDto hallMaster;

	private String weightNameDefaultLang;

	private String weightNamePreferLang;

	private String weightNameSupportiveLang;

	private Long inputTransferToHallId;

}