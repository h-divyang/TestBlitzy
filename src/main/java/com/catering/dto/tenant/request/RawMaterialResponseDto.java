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
public class RawMaterialResponseDto extends OnlyIdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private MeasurementDto measurement;

	private Double supplierRate;

}