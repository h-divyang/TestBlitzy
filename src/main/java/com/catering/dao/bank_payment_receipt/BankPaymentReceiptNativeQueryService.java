package com.catering.dao.bank_payment_receipt;

import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;

/**
 * Service interface for interacting with the `BankPaymentReceiptNativeQueryDao` to execute custom queries 
 * related to `BankPaymentReceiptNativeQuery`. This interface defines methods for fetching supplier contact 
 * details related to bank payment receipts.
 */
public interface BankPaymentReceiptNativeQueryService {

	/**
	 * Retrieves the supplier contact details for a given bank payment receipt by invoking the corresponding 
	 * query method from the DAO layer.
	 * This method returns a {@link CashBankPaymentReceiptCommonResultListDto} containing the contact's name in 
	 * multiple languages and the total amount for the specified `bankPaymentReceiptId`.
	 * 
	 * @param bankPaymentReceiptId the ID of the bank payment receipt to fetch contact details for.
	 * @return {@link CashBankPaymentReceiptCommonResultListDto} containing the supplier's contact details and the total amount.
	 */
	CashBankPaymentReceiptCommonResultListDto getSuppilerContact(Long bankPaymentReceiptId);

}