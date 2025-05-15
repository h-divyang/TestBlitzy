package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderGeneralFixRawMaterialResponseContainerDto {

	private Long functionId;

	private List<OrderGeneralFixRawMaterialResponseDto> orderGeneralFixRawMaterial;

}