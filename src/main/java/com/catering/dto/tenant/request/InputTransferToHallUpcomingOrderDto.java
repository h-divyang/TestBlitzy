package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.OnlyIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InputTransferToHallUpcomingOrderDto extends OnlyIdDto {

	private LocalDate eventMainDate;

	private String customerNameDefaultLang;

	private String customerNamePreferLang;

	private String customerNameSupportiveLang;

	private String eventTypeNameDefaultLang;

	private String eventTypeNamePreferLang;

	private String eventTypeNameSupportiveLang;

	public InputTransferToHallUpcomingOrderDto(Long id, LocalDate eventMainDate, String customerNameDefaultLang, String customerNamePreferLang, String customerNameSupportiveLang, 
			String eventTypeNameDefaultLang, String eventTypeNamePreferLang, String eventTypeNameSupportiveLang) {
		super(id);
		this.eventMainDate = eventMainDate;
		this.customerNameDefaultLang = customerNameDefaultLang;
		this.customerNamePreferLang = customerNamePreferLang;
		this.customerNameSupportiveLang = customerNameSupportiveLang;
		this.eventTypeNameDefaultLang = eventTypeNameDefaultLang;
		this.eventTypeNamePreferLang = eventTypeNamePreferLang;
		this.eventTypeNameSupportiveLang = eventTypeNameSupportiveLang;
	}

}