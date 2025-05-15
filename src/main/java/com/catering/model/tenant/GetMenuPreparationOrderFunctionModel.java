package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "order_function")
public class GetMenuPreparationOrderFunctionModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_function_type_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetMenuPreparationFunctionTypeModel functionType;

	@Column(name = "fk_customer_order_details_id")
	private Long bookOrderId;

	@Column(name = "person")
	private Integer person;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "sequence")
	private Integer sequence;

	@Column(name = "copied_function_sequence")
	private Integer copiedFunctionSequence;

}