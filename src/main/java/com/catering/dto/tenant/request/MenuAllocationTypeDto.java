package com.catering.dto.tenant.request;

import javax.validation.constraints.Digits;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuAllocationTypeDto extends IdDto {

	private MenuAllocationDTO menuPreparationMenuItem;

	private Double total;

	private Long fkContactId;

	private Integer counterNo;

	private Double counterPrice;

	private Integer helperNo;

	private Double helperPrice;

	private Double price;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double quantity;

	private Long unit;

	private Boolean isPlate;

}