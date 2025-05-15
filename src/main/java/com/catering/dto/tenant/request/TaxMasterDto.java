package com.catering.dto.tenant.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a TaxMasterDto in the tenant package.
 * It extends the AuditIdDto class and defines the properties of a tax master for DTO (Data Transfer Object) purposes.
 * 
 * @author Neel Bhanderi
 * @since March 2024
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TaxMasterDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 30, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_30)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameDefaultLang;

	@Size(max = 30, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_30)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String namePreferLang;

	@Size(max = 30, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_30)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameSupportiveLang;

	@DecimalMin(value = "0", message = MessagesConstant.VALIDATION_TAX_IS_NOT_VALID)
	@DecimalMax(value = "100", inclusive = true, message = MessagesConstant.VALIDATION_TAX_IS_NOT_VALID)
	private Double cgst;

	@DecimalMin(value = "0", message = MessagesConstant.VALIDATION_TAX_IS_NOT_VALID)
	@DecimalMax(value = "100", inclusive = true, message = MessagesConstant.VALIDATION_TAX_IS_NOT_VALID)
	private Double sgst;

	@DecimalMin(value = "0", message = MessagesConstant.VALIDATION_TAX_IS_NOT_VALID)
	@DecimalMax(value = "100", inclusive = true, message = MessagesConstant.VALIDATION_TAX_IS_NOT_VALID)
	private Double igst;

}