package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialAllocationRequestDto extends IdDto {

	private Long menuPreparationMenuItemId;

	private MenuItemRawMaterialDto menuItemRawMaterialId;

	private Double actualQty;

	private MeasurementDto actualMeasurementId;

	private Double finalQty;

	private MeasurementDto finalMeasurementId;

	private LocalDateTime orderTime;

	private ContactResponseDto contactAgencyId;

	private RawMaterialDto rawMaterialId;

	private GodownDto godown;

}