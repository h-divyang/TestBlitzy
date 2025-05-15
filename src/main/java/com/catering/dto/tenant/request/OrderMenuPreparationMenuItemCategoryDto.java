package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OrderMenuPreparationMenuItemCategoryDto extends AuditIdDto {

	private Long id;

	private Long menuPreparationId;

	private Long menuItemCategoryId;

	private String noteDefaultLang;

	private String notePreferLang;

	private String noteSupportiveLang;

	private String rupees;

}