package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JournalVoucherDetailsDto extends OnlyIdDto {

	private ContactCustomDto contact;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Double amount;

	private boolean transactionType;

	private String remark;

}