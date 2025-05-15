package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.BankPaymentReceiptModel;

/**
 * BankPaymentReceiptRepository is an interface for managing BankPaymentReceiptModel entities.
 * It extends JpaRepository to provide CRUD (Create, Read, Update, Delete) operations
 * and additional query methods for BankPaymentReceiptModel.
 *
 * This repository contains custom query methods to retrieve and filter data related to
 * bank payment receipts based on specific criteria.
 */
public interface BankPaymentReceiptRepository extends JpaRepository<BankPaymentReceiptModel, Long> {

	/**
	 * Retrieves a list of BankPaymentReceiptModel entities based on the given transaction type.
	 *
	 * @param transactionType A boolean indicating the type of transaction;
	 *						  true for bank receipt and false for bank payment.
	 * @return A list of BankPaymentReceiptModel objects associated with the specified transaction type.
	 */
	List<BankPaymentReceiptModel> findByTransactionType(boolean transactionType);

}