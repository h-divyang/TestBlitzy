package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.OnlyIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputTransferToHallRawMaterialDto extends OnlyIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Double weight;

	private RawMaterialDto rawMaterial;

	private MeasurementDto measurement;

}