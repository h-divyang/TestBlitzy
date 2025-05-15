package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto extends IdDto {

	private String contentDefaultLang;

	private String contentPreferLang;

	private String contentSupportiveLang;

	private LocalDateTime createdAt;

	private Boolean isRead;

	private LocalDateTime readAt;

	private Long readBy;

}