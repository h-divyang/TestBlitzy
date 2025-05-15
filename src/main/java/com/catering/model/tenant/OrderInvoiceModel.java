package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing an order invoice.
 * Extends {@link AuditByIdModel} to include audit-related fields.
 *
 * The order invoice includes details such as order ID, SGST, CGST, discount, advance payment, round off,
 * total amount, remark, bill number, bill date, and a list of associated functions.
 *
 * Functions are represented by instances of {@link OrderInvoiceFunctionModel} and are managed through a
 * one-to-many relationship.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_invoice")
public class OrderInvoiceModel extends AuditByIdModel {

	/**
	 * The unique identifier of the associated order.
	 */
	@Column(name = "fk_customer_order_details_id")
	private Long orderId;

	/**
	 * The unique identifier of the associated tax master.
	 */
	@Column(name = "fk_tax_master_id")
	private Long taxMasterId;

	/**
	 * The discount applied to the order invoice.
	 */
	@Column(name = "discount")
	private Double discount;

	@Column(name = "grand_total")
	private Double grandTotal;

	/**
	 * The amount paid in advance for the order invoice.
	 */
	@Column(name = "advance_payment")
	private Double advancePayment;

	/**
	 * The round-off value for the order invoice.
	 */
	@Column(name = "round_off")
	private Double roundOff;

	/**
	 * Additional remarks or comments associated with the order invoice.
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * The unique bill number associated with the order invoice.
	 */
	@Column(name = "bill_number")
	private String billNumber;

	/**
	 * The date when the order invoice was generated.
	 */
	@Column(name = "bill_date")
	private LocalDate billDate;

	@Column(name = "po_number")
	private String poNumber;

	@Column(name = "company_register_address")
	private String companyRegisteredAddress;

	@Column(name = "contact_person_name")
	private String contactPersonName;

	@Column(name = "contact_person_mobile_number")
	private String contactPersonMobileNo;

	/**
	 * List of functions associated with the order invoice. Managed through a one-to-many relationship with {@link OrderInvoiceFunctionModel}.
	 */
	@OneToMany(mappedBy = "orderInvoiceId", cascade = CascadeType.ALL, orphanRemoval = true)
	List<OrderInvoiceFunctionModel> functions;

}