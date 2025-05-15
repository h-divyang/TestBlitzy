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
public class JournalVoucherDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private LocalDate voucherDate;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	List<JournalVoucherDetailsDto> journalVoucherDetails;

}