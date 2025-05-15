package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.constant.RegexConstant;
import com.catering.dto.audit.AuditIdDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing the request for creating or updating order invoices.
 * Extends {@link AuditIdDto} to include audit-related fields.
 *
 * The class encapsulates information required to create or update an order invoice, including details such as
 * order ID, tax details (sgst, cgst), discount, advance payment, round-off, total amount, remarks, bill number,
 * bill date, and a list of order invoice functions.
 *
 * This DTO is typically used to transfer order invoice request data between different layers of the application,
 * especially during the creation or update of order invoices.
 *
 * The class includes validation annotations to ensure that the provided data meets certain criteria.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderInvoiceRequestDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Long orderId;

	private Long taxMasterId;

	private Double discount;

	private Double grandTotal;

	private Double advancePayment;

	private Double roundOff;

	@Size(max = 255, message = MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String remark;

	@NotBlank(message = MessagesFieldConstants.COMMON_FIELD_BILL_NUMBER + MessagesConstant.VALIDATION_NOT_BLANK)
	@Size(max = 20, message = MessagesFieldConstants.COMMON_FIELD_BILL_NUMBER + MessagesConstant.VALIDATION_MAX_LENGTH_20)
	private String billNumber;

	private LocalDate billDate;

	@Size(max = 20, message = MessagesFieldConstants.INVOICE_PURCHASE_ORDER_NUMBER + MessagesConstant.VALIDATION_MAX_LENGTH_20)
	private String poNumber;

	@Size(min = 2, message = MessagesFieldConstants.INVOICE_COMPANY_REGISTER_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.INVOICE_COMPANY_REGISTER_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String companyRegisteredAddress;

	@Size(max = 50, message = MessagesFieldConstants.INVOICE_CONTACT_PERSON_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.INVOICE_CONTACT_PERSON_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String contactPersonName;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 10, min = 10, message = MessagesFieldConstants.INVOICE_CONTACT_PERSON_MOBILE_NO + MessagesConstant.VALIDATION_LENGTH_10_ONLY)
	private String contactPersonMobileNo;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	List<OrderInvoiceFunctionRequestDto> functions;

}