package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto extends AuditDto {

	private Long bookOrderId;

	private String notesDefaultLang;

	private String notesPreferLang;

	private String notesSupportiveLang;

}