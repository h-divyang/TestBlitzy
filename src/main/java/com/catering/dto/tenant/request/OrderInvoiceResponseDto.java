package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing the response for order invoices.
 * Extends {@link IdDto} to include an identifier field.
 *
 * The class encapsulates information about an order invoice, including details such as
 * order ID, tax details (sgst, cgst), discount, advance payment, round-off, total amount,
 * remarks, bill number, and bill date. Additionally, it includes a list of order invoice functions
 * and their total, SGST total, and CGST total.
 *
 * This DTO is typically used to transfer order invoice response data between different layers of the application.
 *
 * The class provides a constructor for initializing its fields.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
public class OrderInvoiceResponseDto extends IdDto {

	private Long orderId;

	private Long taxMasterId;

	private Double discount;

	private Double grandTotal;

	@Setter
	private Double advancePayment;

	private Double roundOff;

	private Double total;

	private String remark;

	@Setter
	private List<OrderInvoiceFunctionResponseDto> functions;

	@Setter
	private Double functionTotal;

	@Setter
	private String billNumber;

	private LocalDate billDate;

	private String poNumber;

	private String companyRegisteredAddress;

	private String contactPersonName;

	private String contactPersonMobileNo;

	/**
	 * Constructs an {@code OrderInvoiceResponseDto} with the specified parameters.
	 *
	 * @param id             The identifier of the order invoice.
	 * @param orderId        The unique identifier of the order.
	 * @param sgst           The SGST (State Goods and Services Tax) amount.
	 * @param cgst           The CGST (Central Goods and Services Tax) amount.
	 * @param discount       The discount amount.
	 * @param advancePayment The advance payment amount.
	 * @param roundOff       The round-off amount.
	 * @param total          The total amount of the order invoice.
	 * @param remark         Remarks associated with the order invoice.
	 * @param billNumber     The bill number associated with the order invoice.
	 * @param billDate       The date when the bill was generated.
	 */
	public OrderInvoiceResponseDto(Long id, Long orderId, Long taxMasterId, Double discount, Double grandTotal, Double advancePayment, Double roundOff, Double total, String remark, String billNumber, LocalDate billDate, String poNumber, String companyRegisteredAddress, String contactPersonName, String contactPersonMobileNo) {
		setId(id);
		this.orderId = orderId;
		this.taxMasterId = taxMasterId;
		this.discount = discount;
		this.grandTotal = grandTotal;
		this.advancePayment = advancePayment;
		this.roundOff = roundOff;
		this.total = total;
		this.remark = remark;
		this.billNumber = billNumber;
		this.billDate = billDate;
		this.poNumber = poNumber;
		this.companyRegisteredAddress = companyRegisteredAddress;
		this.contactPersonName = contactPersonName;
		this.contactPersonMobileNo = contactPersonMobileNo;
	}

}