package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgencyAllocationDto {

	private Long rawMaterialAllocationId;

	private Long contactAgencyId;

	private Boolean isExtra;

	private Long rawMaterialId;

	private Long godownId;

	private LocalDateTime orderTime;

}