package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateWiseOrderBookingReportDto {

	public String mobileNumber;

	public LocalDateTime date;

	public String functions;

	public String pax;

	public String party;

	public String venue;

}