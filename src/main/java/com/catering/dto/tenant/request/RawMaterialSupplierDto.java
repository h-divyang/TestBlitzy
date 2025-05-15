package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RawMaterialSupplierDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_CONTACT_NOT_BLANK)
	private ContactResponseDto contact;

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_NOT_BLANK)
	private RawMaterialDto rawMaterial;

	private Boolean isDefault;

}