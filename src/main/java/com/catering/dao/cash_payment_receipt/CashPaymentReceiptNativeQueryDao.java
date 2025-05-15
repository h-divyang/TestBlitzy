package com.catering.dao.cash_payment_receipt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;

/**
 * This interface defines a set of native SQL queries related to cash payment receipts and associated voucher details.
 * It extends the {@link JpaRepository} to provide standard CRUD operations for the entity {@link CashPaymentReceiptNativeQuery}.
 * 
 * The queries return various DTOs, such as {@link PaymentContactCustomDto}, {@link VoucherNumberDto}, 
 * and {@link CashBankPaymentReceiptCommonResultListDto}, which contain the relevant contact, voucher, and payment details.
 * 
 * @see JpaRepository
 * @see CashPaymentReceiptNativeQuery
 * @see PaymentContactCustomDto
 * @see VoucherNumberDto
 * @see CashBankPaymentReceiptCommonResultListDto
 */
public interface CashPaymentReceiptNativeQueryDao extends JpaRepository<CashPaymentReceiptNativeQuery, Long> {

	/**
	 * Fetches the contact details related to labour allocation orders.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for labour allocation.
	 */
	@Query(name = "fetchContactOfLabourAllocation", nativeQuery = true)
	List<PaymentContactCustomDto> fetchContactOfLabourAllocation();

	/**
	 * Fetches the contact details based on the specified order type.
	 * 
	 * @param orderTypeId the ID of the order type.
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for the specified order type.
	 */
	@Query(name = "fetchContactOfOrderByOrderType", nativeQuery = true)
	List<PaymentContactCustomDto> fetchContactOfOrderByOrderType(int orderTypeId);

	/**
	 * Fetches the contact details related to purchase bills.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for purchase bills.
	 */
	@Query(name = "fetchContactOfPurchaseBill", nativeQuery = true)
	List<PaymentContactCustomDto> fetchContactOfPurchaseBill();

	/**
	 * Fetches the contact details related to invoices.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for invoices.
	 */
	@Query(name = "fetchContactOfInvoice", nativeQuery = true)
	List<PaymentContactCustomDto> fetchContactOfInvoice();

	/**
	 * Fetches the contact details related to debit notes.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing the contact details for debit notes.
	 */
	@Query(name = "fetchContactOfDebitNote", nativeQuery = true)
	List<PaymentContactCustomDto> fetchContactOfDebitNote();

	/**
	 * Fetches a list of all payment contacts.
	 * 
	 * @return a list of {@link PaymentContactCustomDto} containing all payment contacts.
	 */
	@Query(name = "fetchPaymentContactList", nativeQuery = true)
	List<PaymentContactCustomDto> fetchPaymentContactList();

	/**
	 * Fetches the voucher number details related to purchase bills.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for purchase bills.
	 */
	@Query(name = "fetchVoucherNumberOfPurchaseBill", nativeQuery = true)
	List<VoucherNumberDto> fetchVoucherNumberOfPurchaseBill();

	/**
	 * Fetches the voucher number details related to orders based on the order type and voucher type.
	 * 
	 * @param orderTypeId the ID of the order type.
	 * @param voucherTypeId the ID of the voucher type.
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for orders based on the specified criteria.
	 */
	@Query(name = "fetchVoucherNumberOfOrderByOrderType", nativeQuery = true)
	List<VoucherNumberDto> fetchVoucherNumberOfOrderByOrderType(int orderTypeId, int voucherTypeId);

	/**
	 * Fetches the voucher number details related to labour allocation orders.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for labour allocation.
	 */
	@Query(name = "fetchVoucherNumberOfLabourAllocation", nativeQuery = true)
	List<VoucherNumberDto> fetchVoucherNumberOfLabourAllocation();

	/**
	 * Fetches the voucher number details related to debit notes.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for debit notes.
	 */
	@Query(name = "fetchVoucherNumberOfDebitNote", nativeQuery = true)
	List<VoucherNumberDto> fetchVoucherNumberOfDebitNote();

	/**
	 * Fetches the voucher number details related to invoices.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for invoices.
	 */
	@Query(name = "fetchVoucherNumberOfInvoice", nativeQuery = true)
	List<VoucherNumberDto> fetchVoucherNumberOfInvoice();

	/**
	 * Fetches the voucher number details related to advance payments.
	 * 
	 * @return a list of {@link VoucherNumberDto} containing voucher numbers for advance payments.
	 */
	@Query(name = "fetchVoucherNumberOfAdvancePayment", nativeQuery = true)
	List<VoucherNumberDto> fetchVoucherNumberOfAdvancePayment();

	/**
	 * Checks if a given voucher number is used in cash or bank payment receipts, based on specific voucher types.
	 * 
	 * @param voucherNumber the voucher number to check.
	 * @param voucherTypes a list of voucher types to check against.
	 * @return an integer representing the total count of occurrences of the voucher number in cash or bank payment receipts.
	 *         Returns 0 if no such occurrences exist.
	 */
	@Query(value = "SELECT ( "
			+ "  (SELECT COUNT(1) FROM cash_payment_receipt_details WHERE voucher_number = :voucherNumber AND voucher_type IN (:voucherTypes)) "
			+ "  + "
			+ "  (SELECT COUNT(1) FROM bank_payment_receipt_details WHERE voucher_number = :voucherNumber AND voucher_type IN (:voucherTypes)) "
			+ "  + "
			+ "  (SELECT COUNT(1) FROM cash_payment_receipt_details WHERE voucher_number = (SELECT id FROM order_invoice WHERE fk_customer_order_details_id = :voucherNumber) AND voucher_type IN (:voucherTypes)) "
			+ "  + "
			+ "  (SELECT COUNT(1) FROM bank_payment_receipt_details WHERE voucher_number = (SELECT id FROM order_invoice WHERE fk_customer_order_details_id = :voucherNumber) AND voucher_type IN (:voucherTypes)) "
			+ ") > 0", nativeQuery = true)
	Integer isUseInCashOrBank(long voucherNumber, List<Integer> voucherTypes);

	/**
	 * Fetches the supplier contact details associated with a specific cash payment receipt ID.
	 * 
	 * @param cashPaymentReceiptId the ID of the cash payment receipt.
	 * @return an instance of {@link CashBankPaymentReceiptCommonResultListDto} containing the supplier contact details for the specified cash payment receipt.
	 */
	@Query(name = "getSuppilerContactOfCashPaymentReceiptDetails", nativeQuery = true)
	CashBankPaymentReceiptCommonResultListDto getSuppilerContactOfCashPaymentReceiptDetails(Long cashPaymentReceiptId);

}