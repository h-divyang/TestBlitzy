package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseOutsideOrderReportDto {

	private String supplierName;

	private String mobileNumber;

	private LocalDateTime orderDate;

	private String timeInWord;

	private String venue;

	private String menuItemName;

	private Double quantity;

	private String unit;

	private String menuItemCategory;

	private Double rate;

	private Double total;

	private Integer decimalLimitQty;

	public DateWiseOutsideOrderReportDto(String supplierName, String mobileNumber, 
			LocalDateTime orderDate, String timeInWord, String venue, String menuItemName, Double quantity, String unit, String menuItemCategory, Double rate, Double total, Integer decimalLimitQty) {
		this.supplierName = supplierName;
		this.mobileNumber = mobileNumber;
		this.orderDate = orderDate;
		this.timeInWord = timeInWord;
		this.venue = venue;
		this.menuItemName = menuItemName;
		this.quantity = quantity;
		this.unit = unit;
		this.menuItemCategory = menuItemCategory;
		this.rate = rate;
		this.total = total;
		this.decimalLimitQty = decimalLimitQty;
	}

}