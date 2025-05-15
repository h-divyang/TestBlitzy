package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentSubscriptionDto {

	private int subscriptionType;

	private Long subscriptionId;

	private String plan;

	private Float totalPrice;

	private Float planPrice;

	private Float extraUsersPrice;

	private Integer totalUsers;

	private Integer planUsers;

	private Integer extraUsers;

	private Float totalExtraUsersPrice;

	private LocalDate activeDate;

	private LocalDate dueDate;

}