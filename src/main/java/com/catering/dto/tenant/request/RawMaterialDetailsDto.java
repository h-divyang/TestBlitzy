package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDetailsDto {

	private Boolean isGeneralFixRawMaterial;

	private Long menuPreparationMenuItemId;

	private Long menuItemRawMaterialId;

	private Double finalQty;

	private Long finalMeasurementId;

	private Long functionId;

	private Long rawMaterialId;

}
