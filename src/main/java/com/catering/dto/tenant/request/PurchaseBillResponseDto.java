package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PurchaseBillResponseDto extends AuditIdDto {

	private LocalDate billDate;

	private String billNumber;

	private Long purchaseOrderId;

	private ContactResponseDto contactSupplier;

	private Double amount;

	private Double taxableAmount;

	private Double totalAmount;

	private String remark;

	private Double discount;

	private Double grandTotal;

	private Double extraExpense;

	private Double roundOff;

	private Double total;

}