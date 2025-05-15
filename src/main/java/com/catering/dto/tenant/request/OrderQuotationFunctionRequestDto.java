package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderQuotationFunctionRequestDto extends AuditIdDto {

	private Long orderQuotationId;

	private Long orderFunctionId;

	private String amount;

	private Integer person;

	private Double extra;

	private Double rate;

	private String chargesName;

	private LocalDateTime date;

}