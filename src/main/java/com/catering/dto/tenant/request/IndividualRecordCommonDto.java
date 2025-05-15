package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualRecordCommonDto {

	private String contactName;

	private String customerNumber;

	private String venue;

	private Long invoiceNumber;

	private LocalDate purchaseDate;

	private String rawMaterial;

	private Double weight;

	private String measurement;

	private int decimalLimitQty;

	private LocalDate deliveryDate;

	private Double price;

	private Double cgst;

	private Double cgstPrice;

	private Double sgst;

	private Double sgstPrice;

	private Double amount;

	private String gstNumber;

	private String panNumber;

	private Double igst;

	private Double igstPrice;

	private Boolean isPurchaseBill;

	private String remark;

	private Double discount;

	private Double extraExpense;

	private Double roundOff;

	private Double grandTotal;

	public IndividualRecordCommonDto(String contactName, String customerNumber, String venue, Long invoiceNumber,
			LocalDate purchaseDate, String rawMaterial, Double weight, String measurement, int decimalLimitQty, LocalDate deliveryDate,
			Double price, Double cgst, Double cgstPrice, Double sgst, Double sgstPrice, Double amount,
			String gstNumber, String panNumber) {
		this.contactName = contactName;
		this.customerNumber = customerNumber;
		this.venue = venue;
		this.invoiceNumber = invoiceNumber;
		this.purchaseDate = purchaseDate;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
		this.deliveryDate = deliveryDate;
		this.price = price;
		this.cgst = cgst;
		this.cgstPrice = cgstPrice;
		this.sgst = sgst;
		this.sgstPrice = sgstPrice;
		this.amount = amount;
		this.gstNumber = gstNumber;
		this.panNumber = panNumber;
	}

	public IndividualRecordCommonDto(String contactName, String customerNumber, String venue, Long invoiceNumber,
			LocalDate purchaseDate, String rawMaterial, Double weight, String measurement, int decimalLimitQty, LocalDate deliveryDate,
			Double price, Double igst, Double igstPrice, Double amount,
			String gstNumber, String panNumber) {
		this.contactName = contactName;
		this.customerNumber = customerNumber;
		this.venue = venue;
		this.invoiceNumber = invoiceNumber;
		this.purchaseDate = purchaseDate;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
		this.deliveryDate = deliveryDate;
		this.price = price;
		this.igst = igst;
		this.igstPrice = igstPrice;
		this.amount = amount;
		this.gstNumber = gstNumber;
		this.panNumber = panNumber;
	}

	public IndividualRecordCommonDto(String contactName, String customerNumber, String venue, Long invoiceNumber,
			LocalDate purchaseDate, String rawMaterial, Double weight, String measurement, int decimalLimitQty,
			Double price, Double cgst, Double cgstPrice, Double sgst, Double sgstPrice, Double amount,
			String gstNumber, String panNumber, Boolean isPurchaseBill) {
		this.contactName = contactName;
		this.customerNumber = customerNumber;
		this.venue = venue;
		this.invoiceNumber = invoiceNumber;
		this.purchaseDate = purchaseDate;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
		this.price = price;
		this.cgst = cgst;
		this.cgstPrice = cgstPrice;
		this.sgst = sgst;
		this.sgstPrice = sgstPrice;
		this.amount = amount;
		this.gstNumber = gstNumber;
		this.panNumber = panNumber;
		this.isPurchaseBill = isPurchaseBill;
	}

	public IndividualRecordCommonDto(String contactName, String customerNumber, String venue, Long invoiceNumber,
			LocalDate purchaseDate, String rawMaterial, Double weight, String measurement, int decimalLimitQty,
			Double price, Double igst, Double igstPrice, Double amount,
			String gstNumber, String panNumber, Boolean isPurchaseBill) {
		this.contactName = contactName;
		this.customerNumber = customerNumber;
		this.venue = venue;
		this.invoiceNumber = invoiceNumber;
		this.purchaseDate = purchaseDate;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
		this.price = price;
		this.igst = igst;
		this.igstPrice = igstPrice;
		this.amount = amount;
		this.gstNumber = gstNumber;
		this.panNumber = panNumber;
		this.isPurchaseBill = isPurchaseBill;
	}

	/*Constructor for purchase bill record report with different state*/
	public IndividualRecordCommonDto(String contactName, String customerNumber, String venue, Long invoiceNumber,
			LocalDate purchaseDate, String rawMaterial, Double weight, String measurement, int decimalLimitQty,
			Double price, Double igst, Double igstPrice, Double amount, Double discount, Double extraExpense, Double roundOff,
			Double grandTotal, String gstNumber, String panNumber, Boolean isPurchaseBill, String remark) {
		super();
		this.contactName = contactName;
		this.customerNumber = customerNumber;
		this.venue = venue;
		this.invoiceNumber = invoiceNumber;
		this.purchaseDate = purchaseDate;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
		this.price = price;
		this.igst = igst;
		this.igstPrice = igstPrice;
		this.amount = amount;
		this.discount = discount;
		this.extraExpense = extraExpense;
		this.roundOff = roundOff;
		this.grandTotal = grandTotal;
		this.gstNumber = gstNumber;
		this.panNumber = panNumber;
		this.isPurchaseBill = isPurchaseBill;
		this.remark = remark;
	}

	/*Constructor for purchase bill record report with same state*/
	public IndividualRecordCommonDto(String contactName, String customerNumber, String venue, Long invoiceNumber,
			LocalDate purchaseDate, String rawMaterial, Double weight, String measurement, int decimalLimitQty,
			Double price, Double cgst, Double cgstPrice, Double sgst, Double sgstPrice,
			Double amount, Double discount, Double extraExpense, Double roundOff, Double grandTotal,
			String gstNumber, String panNumber, Boolean isPurchaseBill, String remark) {
		super();
		this.contactName = contactName;
		this.customerNumber = customerNumber;
		this.venue = venue;
		this.invoiceNumber = invoiceNumber;
		this.purchaseDate = purchaseDate;
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
		this.decimalLimitQty = decimalLimitQty;
		this.price = price;
		this.cgst = cgst;
		this.cgstPrice = cgstPrice;
		this.sgst = sgst;
		this.sgstPrice = sgstPrice;
		this.amount = amount;
		this.discount = discount;
		this.extraExpense = extraExpense;
		this.roundOff = roundOff;
		this.grandTotal = grandTotal;
		this.gstNumber = gstNumber;
		this.panNumber = panNumber;
		this.isPurchaseBill = isPurchaseBill;
		this.remark = remark;
	}

}