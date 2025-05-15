package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Data transfer object (DTO) representing a request for a purchase order.
 * Extends AuditIdDto.
 * 
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PurchaseOrderRequestDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_PURCHASE_DATE_NOT_BLANK)
	private LocalDate purchaseDate;

	@NotNull(message = MessagesConstant.VALIDATION_CONTACT_NOT_BLANK)
	private CommonMultiLanguageDto contactSupplier;

	private List<PurchaseOrderRawMaterialRequestDto> purchaseOrderRawMaterialList;

}