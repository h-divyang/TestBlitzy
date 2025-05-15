package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.OrderProformaInvoiceModel;

/**
 * Repository interface for managing {@link OrderProformaInvoiceModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderProformaInvoiceModel}.
 * It includes custom methods for managing advance payments, checking the existence of bill numbers, and verifying the association with orders.
 */
public interface OrderProformaInvoiceRepository extends JpaRepository<OrderProformaInvoiceModel, Long> {

	/**
	 * Checks if a bill number exists, excluding the specified ID.
	 * 
	 * This method checks whether a bill number already exists in the repository, excluding the invoice with the specified ID.
	 * It is useful for verifying if a bill number is already in use while allowing updates to an existing record.
	 * 
	 * @param billNumber the bill number to check for existence
	 * @param id the ID to exclude from the check
	 * @return {@code true} if a record with the given bill number exists and is not the specified ID; {@code false} otherwise
	 */
	boolean existsByBillNumberAndIdNot(String billNumber, Long id);

	/**
	 * Checks if a bill number exists.
	 * 
	 * This method checks whether a given bill number already exists in the repository.
	 * 
	 * @param billNumber the bill number to check for existence
	 * @return {@code true} if a record with the given bill number exists; {@code false} otherwise
	 */
	boolean existsByBillNumber(String billNumber);

	/**
	 * Checks if an invoice exists for a given order ID.
	 * 
	 * This method checks whether an {@link OrderProformaInvoiceModel} exists for the specified order ID.
	 * 
	 * @param id the ID of the order to check
	 * @return {@code true} if an invoice exists for the order; {@code false} otherwise
	 */
	boolean existsByOrderId(Long id);

	/**
	 * Updates the advance payment amount for a given order.
	 * 
	 * This method updates the advance payment for the order based on the action ID and amount. The advance payment is
	 * either added or subtracted depending on the action ID (0 for addition, non-zero for subtraction).
	 * 
	 * @param actionId the action to perform (0 for addition, non-zero for subtraction)
	 * @param amount the amount to add or subtract from the advance payment
	 * @param orderId the ID of the order to update the advance payment for
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE order_proforma_invoice opi "
			+ "SET advance_payment = "
			+ "CASE "
			+ "WHEN :actionId = 0 THEN "
			+ "IFNULL(advance_payment, 0) + :amount "
			+ "ELSE "
			+ "IFNULL(advance_payment, 0) - :amount "
			+ "END "
			+ "WHERE fk_customer_order_details_id = :orderId", nativeQuery = true)
	void updateAdvancePayment(@Param("actionId") int actionId, @Param("amount") double amount, @Param("orderId") long orderId);

	/**
	 * Retrieves the total advance payment for a given order.
	 * 
	 * This method calculates the total advance payment for the specified order by summing the amounts from both cash and bank payment receipts.
	 * If the order has a proforma invoice, the advance payment amount is returned as 0.
	 * 
	 * @param orderId the ID of the order to retrieve the advance payment for
	 * @return the total advance payment amount for the order
	 */
	@Query(value = "SELECT "
			+ "CASE "
			+ "WHEN :orderId IN (SELECT fk_customer_order_details_id FROM order_proforma_invoice) THEN 0 "
			+ "ELSE "
			+ "COALESCE((SELECT SUM(cprd.amount) FROM cash_payment_receipt_details cprd WHERE cprd.voucher_number = :orderId AND cprd.voucher_type = 7), 0) "
			+ "+ "
			+ "COALESCE((SELECT SUM(bprd.amount) FROM bank_payment_receipt_details bprd WHERE bprd.voucher_number = :orderId AND bprd.voucher_type = 7), 0) "
			+ "END AS total_amount;", nativeQuery = true)
	double getAdvancePayment(@Param("orderId") Long orderId);

}