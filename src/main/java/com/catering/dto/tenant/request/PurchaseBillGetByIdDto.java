package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseBillGetByIdDto extends AuditIdDto {

	private LocalDate billDate;

	private String billNumber;

	private Long purchaseOrderId;

	private ContactResponseDto contactSupplier;

	private Double totalAmount;

	private String remark;
	
	private Double discount;

	private Double grandTotal;

	private Double extraExpense;

	private Double roundOff;

	private Double total;

	private List<PurchaseBillRawMaterialRequestDto> purchaseBillRawMaterialList;

}