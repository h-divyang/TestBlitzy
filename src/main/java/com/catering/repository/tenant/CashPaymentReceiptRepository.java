package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.CashPaymentReceiptModel;

/**
 * CashPaymentReceiptRepository provides methods for performing CRUD (Create, Read, Update, Delete)
 * operations and custom queries on CashPaymentReceiptModel entities.
 *
 * This interface extends JpaRepository to inherit basic functionalities for database interactions.
 */
public interface CashPaymentReceiptRepository extends JpaRepository<CashPaymentReceiptModel, Long> {

	/**
	 * Retrieves a list of CashPaymentReceiptModel entities filtered by the specified transaction type.
	 *
	 * @param transactionType The type of transaction to filter the receipts by;
	 *						  true for a specific transaction type and false for another.
	 * @return A list of CashPaymentReceiptModel entities that match the given transaction type,
	 *		   or an empty list if no matching entities are found.
	 */
	List<CashPaymentReceiptModel> findByTransactionType(boolean transactionType);

}