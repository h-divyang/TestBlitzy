package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionTransactionDto extends IdDto {

	private Integer extraUsers;

	private Long subscriptionId;

	private Long subscriptionIdPrevious;

	private Long subscriptionType;

	private LocalDate startDate;

	private LocalDate endDate;

	private String updatedRecord;

}