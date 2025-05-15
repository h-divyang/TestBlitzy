package com.catering.dto.tenant.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object (DTO) representing a request for a purchase order raw material.
 * Extends OnlyIdDto.
 * 
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderRawMaterialRequestDto extends OnlyIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_NOT_BLANK)
	private RawMaterialDto rawMaterial;

	@Size(max = 8, message = MessagesConstant.VALIDATION_HSN_CODE_MAX_8_LENGTH)
	private String hsnCode;

	@NotNull(message = MessagesConstant.VALIDATION_WEIGHT_NOT_BLANK)
	private Double weight;

	@NotNull(message = MessagesConstant.VALIDATION_MEASUREMENT_NOT_BLANK)
	private MeasurementDto measurement;

	@NotNull(message = MessagesConstant.VALIDATION_PRICE_NOT_BLANK)
	private Double price;

	@NotNull(message = MessagesConstant.VALIDATION_DELIVERY_DATE_NOT_BLANK)
	private LocalDate deliveryDate;

	private TaxMasterDto taxMaster;

	@NotNull(message = MessagesConstant.VALIDATION_TOTAL_AMOUNT_NOT_BLANK)
	private Double totalAmount;

}