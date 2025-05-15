package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunctionPerOrderDto extends CommonDataForDropDownDto {

	private Long orderFunctionId;

	private LocalDateTime orderDate;

	public FunctionPerOrderDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long orderFunctionId, LocalDateTime orderDate) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.orderFunctionId = orderFunctionId;
		this.orderDate = orderDate;
	}

}