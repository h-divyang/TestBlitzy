package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;
import com.catering.dto.audit.IdDto;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderProformaInvoiceResponseDto extends IdDto {

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
	private List<OrderProformaInvoiceFunctionResponseDto> functions;

	@Setter
	private Double functionTotal;

	@Setter
	private String billNumber;

	private LocalDate billDate;

	private String poNumber;

	private String companyRegisteredAddress;

	private String contactPersonName;

	private String contactPersonMobileNo;

	public OrderProformaInvoiceResponseDto(Long id, Long orderId, Long taxMasterId, Double discount, Double grandTotal, Double advancePayment, Double roundOff, Double total, String remark, String billNumber, LocalDate billDate, String poNumber, String companyRegisteredAddress, String contactPersonName, String contactPersonMobileNo) {
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