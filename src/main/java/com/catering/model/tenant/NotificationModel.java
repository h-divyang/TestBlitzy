package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class NotificationModel extends AuditIdModelOnly {

	@Column(name = "content_default_lang")
	private String contentDefaultLang;

	@Column(name = "content_prefer_lang")
	private String contentPreferLang;

	@Column(name = "content_supportive_lang")
	private String contentSupportiveLang;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "is_read")
	private Boolean isRead;

	@Column(name = "read_at")
	private LocalDateTime readAt;

	@Column(name = "read_by")
	private Long readBy;

}