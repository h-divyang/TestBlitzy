package com.catering.model.tenant;

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
@Table(name = "order_function")
public class SaveMenuPreparationOrderFunctionModel extends AuditIdModelOnly {

	@Column(name = "fk_function_type_id")
	private Long functionTypeId;

	@Column(name = "person")
	private Integer person;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "fk_customer_order_details_id")
	private Long orderId;

}