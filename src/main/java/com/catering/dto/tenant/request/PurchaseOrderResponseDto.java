package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object (DTO) representing a response for a purchase order.
 * Extends AuditIdDto.
 *
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderResponseDto extends AuditIdDto {

	private LocalDate purchaseDate;

	private ContactResponseDto contactSupplier;

	private Double amount;

	private Double taxableAmount;

	private Double totalAmount;

}