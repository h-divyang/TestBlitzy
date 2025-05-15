package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.common.FilterDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExtraExpenseParameterDto extends FilterDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Long orderFunctionId;

}