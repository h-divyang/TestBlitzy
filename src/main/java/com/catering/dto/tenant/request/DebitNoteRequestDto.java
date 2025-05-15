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

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DebitNoteRequestDto extends AuditIdDto {

	private Long purchaseBillId;

	@NotNull(message = MessagesConstant.VALIDATION_PURCHASE_DATE_NOT_BLANK)
	private LocalDate billDate;

	private String billNumber;

	@NotNull(message = MessagesConstant.VALIDATION_CONTACT_NOT_BLANK)
	private CommonMultiLanguageDto contactSupplier;

	private List<DebitNoteRawMaterialRequestDto> debitNoteRawMaterialList;

}