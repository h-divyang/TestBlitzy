package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for retrieving detailed information about a purchase order.
 * Extends {@link AuditIdDto} to include audit and identifier information.
 *
 * @author Krushali Talaviya
 */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderGetByIdDto extends AuditIdDto {

	private LocalDate purchaseDate;

	private ContactResponseDto contactSupplier;

	private Double totalAmount;

	private List<PurchaseOrderRawMaterialRequestDto> purchaseOrderRawMaterialList;

}