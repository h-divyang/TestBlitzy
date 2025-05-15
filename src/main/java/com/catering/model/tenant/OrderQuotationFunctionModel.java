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
@Table(name = "order_quotation_function")
public class OrderQuotationFunctionModel extends AuditIdModelOnly {

	@Column(name = "fk_order_quotation_id", insertable = false, updatable = false)
	private Long orderQuotationId;

	@JoinColumn(name = "fk_order_quotation_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderQuotationModel orderQuotation;

	@Column(name = "fk_order_function_id")
	private Long orderFunctionId;

	@Column(name = "person")
	private Integer person;

	@Column(name = "extra")
	private Double extra;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "charges_name")
	private String chargesName;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "amount")
	private String amount;

}