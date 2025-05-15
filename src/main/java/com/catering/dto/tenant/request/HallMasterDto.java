package com.catering.dto.tenant.request;

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
 * Hall Master DTO class
 * 
 * @author Rohan Parmar
 * @since February 2024
 * @see AuditIdDto
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class HallMasterDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 70, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_70)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_2_LENGTH)
	private String nameDefaultLang;

	@Size(max = 70, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_70)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_2_LENGTH)
	private String namePreferLang;

	@Size(max = 70, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_70)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_2_LENGTH)
	private String nameSupportiveLang;

	private String image;

	@DecimalMin(value = "0", message = MessagesConstant.VALIDATION_IS_INVALID)
	private Integer capacity;

}