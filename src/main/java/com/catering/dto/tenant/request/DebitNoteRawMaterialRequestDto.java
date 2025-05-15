package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DebitNoteRawMaterialRequestDto extends OnlyIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_NOT_BLANK)
	private RawMaterialDto rawMaterial;

	@NotNull(message = MessagesConstant.VALIDATION_WEIGHT_NOT_BLANK)
	private Double weight;

	@NotNull(message = MessagesConstant.VALIDATION_MEASUREMENT_NOT_BLANK)
	private MeasurementDto measurement;

	@NotNull(message = MessagesConstant.VALIDATION_PRICE_NOT_BLANK)
	private Double price;

	private TaxMasterDto taxMaster;

	@NotNull(message = MessagesConstant.VALIDATION_TOTAL_AMOUNT_NOT_BLANK)
	private Double totalAmount;

}