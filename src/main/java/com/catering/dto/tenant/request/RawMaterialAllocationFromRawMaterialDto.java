package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialAllocationFromRawMaterialDto extends IdDto {

	private Long orderId;

	private Double finalQty;

	private Long finalMeasurementId;

	private Long contactAgencyId;

	private LocalDateTime orderTime;

	private Long godown;

	private Boolean isExtra;

	private Long rawMaterialId;

	private Double total;

}