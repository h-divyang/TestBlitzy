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
public class DebitNoteGetByIdDto extends AuditIdDto {

	private LocalDate billDate;

	private String billNumber;

	private Long purchaseBillId;

	private CommonMultiLanguageDto contactSupplier;

	private Double totalAmount;

	private List<DebitNoteRawMaterialRequestDto> debitNoteRawMaterialList;

}