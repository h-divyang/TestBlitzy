package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.AuditDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCrockeryDto extends AuditDto {

	private Long rawMaterialId;

	private Long orderFunction;

	private Double qty;

	private Long measurementId;

	private Double price;

	private LocalDateTime orderTime;

	private Long agency;

	private Long godown;

}