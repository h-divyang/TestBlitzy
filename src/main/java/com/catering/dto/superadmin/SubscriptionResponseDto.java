package com.catering.dto.superadmin;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionResponseDto extends AuditIdDto {

	private String name;

	private Double amount;

	private String description;

	private Integer userLimit;

	private Double extraUserPrice;

}