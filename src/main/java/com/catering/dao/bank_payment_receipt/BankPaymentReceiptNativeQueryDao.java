package com.catering.dao.bank_payment_receipt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;

/**
 * Data access object (DAO) interface for executing native queries related to `BankPaymentReceiptNativeQuery`.
 * This interface extends {@link JpaRepository} to provide basic CRUD operations, and it also defines custom 
 * native query methods for retrieving supplier contact details for a given bank payment receipt.
 */
public interface BankPaymentReceiptNativeQueryDao extends JpaRepository<BankPaymentReceiptNativeQuery, Long> {

	/**
	 * Fetches the supplier contact details and total amount for a specific bank payment receipt by executing the 
	 * native query `getSuppilerContactOfBankPaymentReceiptDetails`.
	 * This query returns the contact name in multiple languages and the total amount for the specified 
	 * `bankPaymentReceiptId`.
	 * 
	 * @param bankPaymentReceiptId the ID of the bank payment receipt to fetch contact details for.
	 * @return {@link CashBankPaymentReceiptCommonResultListDto} containing the supplier's contact details and the total amount.
	 */
	@Query(name = "getSuppilerContactOfBankPaymentReceiptDetails", nativeQuery = true)
	CashBankPaymentReceiptCommonResultListDto getSuppilerContactOfBankPaymentReceiptDetails(Long bankPaymentReceiptId);

}