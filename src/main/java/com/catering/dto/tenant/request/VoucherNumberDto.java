package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO class representing voucher number for a Cash/Bank (Payment/Receipt).
 *
 * @author Priyansh Patel
 * @since 2024-07-29
 */
@Getter
@Setter
@NoArgsConstructor
public class VoucherNumberDto {

	private Long orderId;

	private PaymentContactCustomDto contact;

	private Double remainAmount;

	private LocalDate eventDate;

	// Explicit constructor for SQL result mapping
	public VoucherNumberDto(Long orderId, Double remainAmount, Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, String mobileNumber, Double currentBalance) {
		this.orderId = orderId;
		this.remainAmount = remainAmount;
		this.contact = new PaymentContactCustomDto(id, nameDefaultLang, namePreferLang, nameSupportiveLang, mobileNumber, currentBalance);
	}

	// Constructor to load voucher no drop down for advance payment
	public VoucherNumberDto(Long orderId, Double remainAmount, LocalDate eventDate, Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, String mobileNumber, Double currentBalance) {
		this.orderId = orderId;
		this.remainAmount = remainAmount;
		this.eventDate = eventDate;
		this.contact = new PaymentContactCustomDto(id, nameDefaultLang, namePreferLang, nameSupportiveLang, mobileNumber, currentBalance);;
	}

}