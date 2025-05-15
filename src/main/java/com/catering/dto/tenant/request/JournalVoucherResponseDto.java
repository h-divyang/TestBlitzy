package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.OnlyIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JournalVoucherResponseDto extends OnlyIdDto {

	private Long id;

	private LocalDate voucherDate;

	private Double totalCr;

	private Double totalDr;

}