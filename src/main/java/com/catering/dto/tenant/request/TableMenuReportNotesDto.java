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
public class TableMenuReportNotesDto extends AuditIdDto {

	private Long orderId;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}