package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DebitNoteResponseDto extends AuditIdDto {

	private LocalDate billDate;	

	private String billNumber;

	private Long purchaseBillId;

	private ContactResponseDto contactSupplier;

	private Double amount;

	private Double taxableAmount;

	private Double totalAmount;

}