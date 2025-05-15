package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO class representing contact of voucher number for a Cash/Bank (Payment/Receipt).
 *
 * @author Priyansh Patel
 * @since 2024-07-29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentContactCustomDto {

	private Long id;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private String mobileNumber;

	private Double currentBalance;

}