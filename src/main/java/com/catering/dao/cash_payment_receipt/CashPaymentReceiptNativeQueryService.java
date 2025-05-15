package com.catering.dao.cash_payment_receipt;

import java.util.List;

import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;

/**
 * Service interface for handling native queries related to cash payment receipts and associated payment contacts.
 * 
 * This interface provides methods to fetch contact details and voucher numbers for various payment-related entities,
 * including labour allocation, orders, purchase bills, invoices, debit notes, and advance payments. It also includes 
 * methods to check if a voucher is used in cash or bank payment receipts and to fetch supplier contact details for 
 * a specific cash payment receipt.
 * 
 * The methods in this interface leverage native SQL queries to interact with the database, providing efficient 
 * and direct access to the required data.
 * 
 * @see CashPaymentReceiptNativeQueryDao for the underlying data access layer.
 */
public interface CashPaymentReceiptNativeQueryService {

	/**
	 * Fetches the contact details related to labour allocation orders.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for labour allocation.
	 */
	List<PaymentContactCustomDto> fetchContactOfLabourAllocation();

	/**
	 * Fetches the contact details based on the specified order type.
	 * 
	 * @param orderTypeId the ID of the order type.
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for the specified order type.
	 */
	List<PaymentContactCustomDto> fetchContactOfOrderByOrderType(int orderTypeId);

	/**
	 * Fetches the contact details related to purchase bills.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for purchase bills.
	 */
	List<PaymentContactCustomDto> fetchContactOfPurchaseBill();

	/**
	 * Fetches the contact details related to invoices.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for invoices.
	 */
	List<PaymentContactCustomDto> fetchContactOfInvoice();

	/**
	 * Fetches the contact details related to debit notes.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for debit notes.
	 */
	List<PaymentContactCustomDto> fetchContactOfDebitNote();

	/**
	 * Fetches a list of all payment contacts.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing all payment contacts.
	 */
	List<PaymentContactCustomDto> fetchPaymentContactList();

	/**
	 * Fetches the voucher number details related to labour allocation orders.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for labour allocation.
	 */
	List<VoucherNumberDto> fetchVoucherNumberOfLabourAllocation();

	/**
	 * Fetches the voucher number details related to orders based on the order type and voucher type.
	 * 
	 * @param orderTypeId the ID of the order type.
	 * @param voucherTypeId the ID of the voucher type.
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for orders based on the specified criteria.
	 */
	List<VoucherNumberDto> fetchVoucherNumberOfOrderByOrderType(int orderTypeId, int voucherTypeId);

	/**
	 * Fetches the voucher number details related to purchase bills.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for purchase bills.
	 */
	List<VoucherNumberDto> fetchVoucherNumberOfPurchaseBill();

	/**
	 * Fetches the voucher number details related to debit notes.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for debit notes.
	 */
	List<VoucherNumberDto> fetchVoucherNumberOfDebitNote();

	/**
	 * Fetches the voucher number details related to invoices.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for invoices.
	 */
	List<VoucherNumberDto> fetchVoucherNumberOfInvoice();

	/**
	 * Fetches the voucher number details related to advance payments.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for advance payments.
	 */
	List<VoucherNumberDto> fetchVoucherNumberOfAdvancePayment();

	/**
	 * Checks if a given voucher number is used in cash or bank payment receipts, based on specific voucher types.
	 * 
	 * @param id the voucher number to check.
	 * @param voucherTypeIds a list of voucher type IDs to check against.
	 * @return an integer representing the total count of occurrences of the voucher number in cash or bank payment receipts.
	 *         Returns 0 if no such occurrences exist.
	 */
	Integer isUseInCashOrBank(long id, List<Integer> voucherTypeIds);

	/**
	 * Fetches the supplier contact details associated with a specific cash payment receipt ID.
	 * 
	 * @param cashPaymentReceiptId the ID of the cash payment receipt.
	 * @return an instance of {@link CashBankPaymentReceiptCommonResultListDto} containing the supplier contact details for the specified cash payment receipt.
	 */
	CashBankPaymentReceiptCommonResultListDto getSuppilerContact(Long cashPaymentReceiptId);

}