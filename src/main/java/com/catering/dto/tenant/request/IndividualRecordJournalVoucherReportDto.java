package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualRecordJournalVoucherReportDto extends OnlyIdDto {

	private String contactName;

	private LocalDate transactionDate;

	private String remark;

	private Double amount;

	private Boolean transactionType;

	public IndividualRecordJournalVoucherReportDto(Long id, String contactName, LocalDate transactionDate, String remark, Double amount, Boolean transactionType) {
		this.setId(id);;
		this.contactName = contactName;
		this.transactionDate = transactionDate;
		this.remark = remark;
		this.amount = amount;
		this.transactionType = transactionType;
	}

}