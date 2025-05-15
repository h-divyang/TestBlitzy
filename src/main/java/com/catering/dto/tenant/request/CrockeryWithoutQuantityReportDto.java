package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrockeryWithoutQuantityReportDto {

	private Integer orderFunctionId;

	private String functionName;

	private String category;

	private String crockeryName;

	private LocalDateTime date;

	private Boolean isMaxPerson;

}