package com.catering.dto.tenant.request;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class LabourShiftDto extends CommonMultiLanguageDto {

	private LocalTime time;

}