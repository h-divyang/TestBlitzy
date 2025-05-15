package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GstSalesPurchaseReportCommonDto {

	private LocalDateTime billDate;

	private Long billNo;

	private String contactName;

	private String gstNumber;

	private Double netTotal;

	private Double cgstRate;

	private Double sgstRate;

	private Double igstRate;

	private Double cgstPrice;

	private Double sgstPrice;

	private Double igstPrice;

	private Double total;

	private int decimalLimitForCurrency;

	public GstSalesPurchaseReportCommonDto(LocalDateTime billDate, Long billNo, String contactName, String gstNumber,
			Double netTotal, Double cgstRate, Double sgstRate, Double igstRate, Double cgstPrice, Double sgstPrice,
			Double igstPrice, Double total, Integer decimalLimitForCurrency) {
		this.billDate = billDate;
		this.billNo = billNo;
		this.contactName = contactName;
		this.gstNumber = gstNumber;
		this.netTotal = netTotal;
		this.cgstRate = cgstRate;
		this.sgstRate = sgstRate;
		this.igstRate = igstRate;
		this.cgstPrice = cgstPrice;
		this.sgstPrice = sgstPrice;
		this.igstPrice = igstPrice;
		this.total = total;
		this.decimalLimitForCurrency = decimalLimitForCurrency;
	}

	public GstSalesPurchaseReportCommonDto(LocalDateTime billDate, Long billNo, String contactName, Double netTotal,
			Double cgstPrice, Double sgstPrice, Double igstPrice, Double total, Integer decimalLimitForCurrency) {
		this.billDate = billDate;
		this.billNo = billNo;
		this.contactName = contactName;
		this.netTotal = netTotal;
		this.cgstPrice = cgstPrice;
		this.sgstPrice = sgstPrice;
		this.igstPrice = igstPrice;
		this.total = total;
		this.decimalLimitForCurrency = decimalLimitForCurrency;
	}

}