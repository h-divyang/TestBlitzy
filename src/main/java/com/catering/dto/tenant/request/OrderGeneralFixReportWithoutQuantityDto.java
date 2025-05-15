package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * GeneralFixReportWithoutQuantityDto.
 * This class represents a Data Transfer Object (DTO). for the General Fix Raw Material Report in an event agency distribution.
 * 
 * @author Amit Chauhan
 * @since 2024-02-16
 */
@Getter 
@Setter
@AllArgsConstructor
public class OrderGeneralFixReportWithoutQuantityDto {

	private Integer orderFunctionId;

	private String functionName;

	private LocalDateTime date;

	private String rawMaterialName;

	private Boolean isMaxPerson;

}