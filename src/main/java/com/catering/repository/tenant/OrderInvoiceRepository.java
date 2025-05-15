package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.OrderInvoiceModel;

/**
 * Repository interface for managing {@link OrderInvoiceModel} entities.
 * Extends the Spring Data JPA {@link JpaRepository} interface, providing CRUD and custom query methods
 * for interacting with the underlying database table associated with {@code OrderInvoiceModel}.
 *
 * This interface allows the application to perform common database operations related to order invoices,
 * such as saving, updating, deleting, and querying order invoice data.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoiceModel, Long>  {

	/**
	 * Checks if an {@link OrderInvoiceModel} exists with the specified bill number, excluding a specific invoice ID.
	 * 
	 * @param billNumber the bill number to check for
	 * @param id the invoice ID to exclude from the check
	 * @return {@code true} if an invoice with the given bill number and not the specified ID exists, otherwise {@code false}
	 */
	boolean existsByBillNumberAndIdNot(String billNumber, Long id);

	/**
	 * Checks if an {@link OrderInvoiceModel} exists with the specified bill number.
	 * 
	 * @param billNumber the bill number to check for
	 * @return {@code true} if an invoice with the given bill number exists, otherwise {@code false}
	 */
	boolean existsByBillNumber(String billNumber);

	/**
	 * Checks if an {@link OrderInvoiceModel} exists for a specific order ID.
	 * 
	 * @param id the order ID to check for
	 * @return {@code true} if an invoice exists for the given order ID, otherwise {@code false}
	 */
	boolean existsByOrderId(Long id);

	/**
	 * Updates the advance payment for a specific customer order based on the provided action ID and amount.
	 * 
	 * @param actionId the action (0 for addition, 1 for subtraction) to update the advance payment
	 * @param amount the amount to add or subtract from the advance payment
	 * @param voucherNumber the voucher number (order ID) to update the advance payment for
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE order_invoice oi "
			+ "SET advance_payment = "
			+ "CASE "
			+ "WHEN :actionId = 0 THEN "
			+ "IFNULL(advance_payment, 0) + :amount "
			+ "ELSE "
			+ "IFNULL(advance_payment, 0) - :amount "
			+ "END "
			+ "WHERE fk_customer_order_details_id = :voucherNumber", nativeQuery = true)
	void updateAdvancePayment(@Param("actionId") int actionId, @Param("amount") double amount, @Param("voucherNumber") long voucherNumber);

	/**
	 * Retrieves the total advance payment for a specific order, including both cash and bank payments.
	 * 
	 * @param orderId the order ID to retrieve the advance payment for
	 * @return the total advance payment amount for the given order ID
	 */
	@Query(value = "SELECT "
			+ "CASE "
			+ "WHEN :orderId IN (SELECT fk_customer_order_details_id FROM order_invoice) THEN 0 "
			+ "ELSE "
			+ "COALESCE((SELECT SUM(cprd.amount) FROM cash_payment_receipt_details cprd WHERE cprd.voucher_number = :orderId AND cprd.voucher_type = 7), 0) "
			+ "+ "
			+ "COALESCE((SELECT SUM(bprd.amount) FROM bank_payment_receipt_details bprd WHERE bprd.voucher_number = :orderId AND bprd.voucher_type = 7), 0) "
			+ "END AS total_amount;", nativeQuery = true)
	double getAdvancePayment(@Param("orderId") Long orderId);

}