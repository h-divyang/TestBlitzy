package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MeasurementWithCustomRangeDto extends MeasurementDto {

	private List<CustomRangeDto> customRange;

}