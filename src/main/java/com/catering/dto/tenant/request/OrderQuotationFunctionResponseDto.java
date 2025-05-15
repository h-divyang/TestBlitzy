package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;

@Getter
public class OrderQuotationFunctionResponseDto extends IdDto {

	private String functionNameDefaultLang;

	private String functionNamePreferLang;

	private String functionNameSupportiveLang;

	private LocalDateTime date;

	private Double person;

	private Double extra;

	private Double rate;

	private String amount;

	private Long orderQuotationId;

	private Long orderFunctionId;

	public OrderQuotationFunctionResponseDto(Long id, String functionNameDefaultLang,
			String functionNamePreferLang, String functionNameSupportiveLang, LocalDateTime date, Double person, Double extra, Double rate,
			String amount, Long orderQuotationId, Long orderFunctionId) {
		setId(id);
		this.functionNameDefaultLang = functionNameDefaultLang;
		this.functionNamePreferLang = functionNamePreferLang;
		this.functionNameSupportiveLang = functionNameSupportiveLang;
		this.date = date;
		this.person = person;
		this.extra = extra;
		this.rate = rate;
		this.amount = amount;
		this.orderQuotationId = orderQuotationId;
		this.orderFunctionId = orderFunctionId;
	}

}