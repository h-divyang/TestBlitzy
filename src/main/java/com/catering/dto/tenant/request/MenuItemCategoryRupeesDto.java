package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemCategoryRupeesDto extends OnlyIdDto {

	private Long categoryId;

	private Long menuPreparationId;

	private Double rupees;

}