package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package List.
 * This DTO is used for transferring necessary list data related to packages.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CustomPackageListResponseDto extends AuditIdDto {

	private Double price;

	private Long totalItems;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}