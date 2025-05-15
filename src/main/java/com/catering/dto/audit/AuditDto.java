package com.catering.dto.audit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class AuditDto {

	private Long id;

	private AuditUserDto create;

	private AuditUserDto update;

	private Integer editCount;

}