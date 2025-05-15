package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuAllocationListDto {

	@Valid
	private List<MenuAllocationDTO> menuAllocations;

}