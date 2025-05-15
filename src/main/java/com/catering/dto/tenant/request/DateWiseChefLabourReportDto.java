package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseChefLabourReportDto extends DateWiseOutsideOrderReportDto {

	private String counterNo;

	private String helperNo;

	private Double counterPrice;

	private Double helperPrice;

	private String unit;

	private Integer decimalLimitQtyForChef;

	public DateWiseChefLabourReportDto(String supplierName, String mobileNumber, LocalDateTime orderDate, String venue,
			String menuItemName, String menuItemCategory, String counterNo, String helperNo, Double counterPrice, Double helperPrice, String timeInWord, String unit, Integer decimalLimitQtyForChef, Double total) {
		super(supplierName, mobileNumber, orderDate, timeInWord, venue, menuItemName, null, null, menuItemCategory, null, total, null);
		this.counterNo = counterNo;
		this.helperNo = helperNo;
		this.counterPrice = counterPrice;
		this.helperPrice = helperPrice;
		this.unit = unit;
		this.decimalLimitQtyForChef = decimalLimitQtyForChef;
	}

}