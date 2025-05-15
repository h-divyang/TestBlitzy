package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuAllocationTypeForNativeQuery {

	private Long id;

	private Long fkOrderMenuPreparationMenuItemId;

	private Long fkContactId;

	private Integer counterNo;

	private Double counterPrice;

	private Integer helperNo;

	private Double helperPrice;

	private Double quantity;

	private Double price;

}