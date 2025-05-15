package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.AuditDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGeneralFixRawMaterialDto extends AuditDto {

	private Long rawMaterialId;

	private Long orderFunctionId;

	private Double qty;

	private Long measurementId;

	private Double price;

	private Long agency;

	private LocalDateTime orderTime;

	private Long godown;

}