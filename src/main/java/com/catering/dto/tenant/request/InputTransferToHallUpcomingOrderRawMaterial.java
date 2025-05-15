package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InputTransferToHallUpcomingOrderRawMaterial {

	private Long rawMaterialId;

	private Double qty;

	private Long qtyMeasurementId;

}