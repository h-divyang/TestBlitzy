package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookingTemplateNotesDto extends AuditIdDto {

	private String notesDefaultLang;

	private String notesPreferLang;

	private String notesSupportiveLang;

}