package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputTransferToHallListWeightDto {

	private Long id;

	private Double weight;

	private String name;

}