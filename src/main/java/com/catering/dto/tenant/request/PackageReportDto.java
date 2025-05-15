package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PackageReportDto {

	private String customPackageName;

	private Long noOfItems;

	private Double customPackagePrice;

	private String menuItemCategory;

	private String menuItem;

}