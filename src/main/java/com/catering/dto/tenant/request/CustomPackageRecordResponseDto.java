package com.catering.dto.tenant.request;

import java.util.List;

import com.catering.dto.audit.IdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package.
 * This DTO is used for transferring data related to packages for record page.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageRecordResponseDto extends IdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private Double price;

	private Long totalItems;

	private List<CustomPackageRecordMenuItemCategoryResponseDto> packageMenuItemCategoryList;

	private List<CustomPackageRecordMenuItemResponseDto> packageMenuItemsList;

}