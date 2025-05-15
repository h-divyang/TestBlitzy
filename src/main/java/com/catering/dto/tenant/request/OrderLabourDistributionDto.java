package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.catering.dto.audit.AuditDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderLabourDistributionDto extends AuditDto {

	@NotNull
	@JsonBackReference
	private OrderFunctionDto orderFunction;

	@NotNull
	private ContactResponseDto contact;

	@NotNull
	private ContactCategoryDto contactCategory;

	@NotNull
	private LabourShiftDto labourShift;

	private GodownDto godown;

	@NotNull
	private LocalDateTime date;

	@NotNull
	private Double labourPrice;

	@NotNull
	private Double quantity;

	private String noteDefaultLang;

	private String notePreferLang;

	private String noteSupportiveLang;

}