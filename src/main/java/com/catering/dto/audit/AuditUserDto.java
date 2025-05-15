package com.catering.dto.audit;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AuditUserDto {

	private LocalDateTime time;

	private String firstNameDefaultLang;

	private String firstNamePreferLang;

	private String firstNameSupportiveLang;

	private String lastNameDefaultLang;

	private String lastNamePreferLang;

	private String lastNameSupportiveLang;

}