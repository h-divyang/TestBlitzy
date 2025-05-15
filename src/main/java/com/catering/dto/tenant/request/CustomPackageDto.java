package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package.
 * This DTO is used for transferring data related to packages.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CustomPackageDto extends CommonMultiLanguageDto {

	private Double price;

	private Long totalItems;

	private List<PackageMenuItemCategoryDto> packageMenuItemCategoryList;

	private List<PackageMenuItemDto> packageMenuItemsList;

}