package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RawMaterialCalculationDto {

	@NotNull
	private Long orderFunctionId;

	@NotNull
	private Long menuItemId;

}