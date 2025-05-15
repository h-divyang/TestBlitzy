package com.catering.dto.superadmin;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CateringPreferencesDto extends AuditIdDto {

	private String name;

	private String timeZone;

	private String developedBy;

}