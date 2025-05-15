package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import com.catering.dto.audit.IdDto;
import lombok.Getter;

@Getter
public class OrderProformaInvoiceFunctionResponseDto extends IdDto {

	private String functionNameDefaultLang;

	private String functionNamePreferLang;

	private String functionNameSupportiveLang;

	private LocalDateTime date;

	private Double person;

	private Double extra;

	private Double rate;

	private Double functionRate;

	private Double amount;

	private Long orderProformaInvoiceId;

	private Long orderFunctionId;

	public OrderProformaInvoiceFunctionResponseDto(Long id, String functionNameDefaultLang,
			String functionNamePreferLang, String functionNameSupportiveLang, LocalDateTime date, Double person, Double extra, Double rate, Double functionRate,
			Double amount, Long orderProformaInvoiceId, Long orderFunctionId) {
		setId(id);
		this.functionNameDefaultLang = functionNameDefaultLang;
		this.functionNamePreferLang = functionNamePreferLang;
		this.functionNameSupportiveLang = functionNameSupportiveLang;
		this.date = date;
		this.person = person;
		this.extra = extra;
		this.rate = rate;
		this.functionRate = functionRate;
		this.amount = amount;
		this.orderProformaInvoiceId = orderProformaInvoiceId;
		this.orderFunctionId = orderFunctionId;
	}

}