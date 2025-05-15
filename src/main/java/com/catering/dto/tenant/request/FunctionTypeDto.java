package com.catering.dto.tenant.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a FunctionTypeDto in the tenant package.
 * It extends the AuditIdDto class and defines the properties of a function master for DTO (Data Transfer Object) purposes.
 * 
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FunctionTypeDto extends CommonMultiLanguageDto {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime time;

	private LocalTime endTime;

	private LabourShiftDto labourShift;

}