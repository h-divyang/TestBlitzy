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

/**
 * Entity class representing the association between an order invoice and its related functions.
 * Extends {@link AuditIdModelOnly} to include audit-related fields.
 *
 * The class is mapped to the "order_invoice_function" table in the database and defines the relationships
 * between an order invoice and its functions, including details such as the order invoice identifier,
 * associated order invoice entity, order function identifier, number of persons, additional charges (extra),
 * rate, and the name of the charges.
 *
 * This entity is used to store and retrieve information about the association between an order invoice and its functions
 * in the database.
 *
 * The class includes a no-argument constructor for creating instances without specifying initial values.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_invoice_function")
public class OrderInvoiceFunctionModel extends AuditIdModelOnly {

	@Column(name = "fk_order_invoice_id", insertable = false, updatable = false)
	private Long orderInvoiceId;

	@JoinColumn(name = "fk_order_invoice_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderInvoiceModel orderInvoice;

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

}