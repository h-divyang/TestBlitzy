package com.catering.model.tenant;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscription_transaction")
public class SubscriptionTransactionModel extends AuditIdModelOnly {

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "extra_users")
	private Integer extraUsers;

	@Column(name = "fk_subscription_id")
	private Long subscriptionId;

	@Column(name = "subscription_type")
	private Long subscriptionType;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "fk_subscription_id_previous", insertable = false, updatable = false)
	private Long subscriptionIdPrevious;

}