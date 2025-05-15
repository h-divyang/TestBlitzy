package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a data transfer object (DTO) for Feedback Report in a menu execution.
 *
 * This DTO contains information about feedback reports, including details such as function name,
 * the number of persons, start date, venue, and party name.
 *
 * @since 2023-09-22
 * @author Krushali Talaviya
 */
@Getter
@Setter
@AllArgsConstructor
public class AdminFeedBackReportDto {

	private Long orderFunctionId;

	private String functionName;

	private Long person;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private String functionAddress;

	private String timeInWord;

}