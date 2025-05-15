package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TwoLanguageMenuReportDto {

	private Long functionId;

	private Long orderId;

	private LocalDate bookingDate;

	private String orderStatus;

	private String customerName;

	private String customerNumber;

	private String venue;

	private String functionName;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private Long person;

	private String functionAddress;

	private String functionNote;

	private Double rate;

	private Long menuTotal;

	private Double totalPrice;

	private String menuItemCategoryNameInEnglish;

	private String menuItemCategoryNameInGujarati;

	private String menuItemInEnglish;

	private String menuItemInGujarati;

	private String menuItemInfo;

	private String menuItemCategoryInfo;

}