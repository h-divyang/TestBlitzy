package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.OrderQuotationModel;

/**
 * Repository interface for managing {@link OrderQuotationModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderQuotationModel}.
 * It includes custom methods for managing advance payments and checking if a quotation exists for a given order.
 */
public interface OrderQuotationRepository extends JpaRepository<OrderQuotationModel, Long> {

	/**
	 * Checks if a quotation exists for the given order ID.
	 * 
	 * This method checks whether an {@link OrderQuotationModel} exists for the specified {@code orderId}.
	 * It is useful for validating if an order quotation has been created for a specific order.
	 * 
	 * @param id the ID of the order to check
	 * @return {@code true} if a quotation exists for the order; {@code false} otherwise
	 */
	boolean existsByOrderId(Long id);

	/**
	 * Updates the advance payment amount for a given order based on the action ID and amount.
	 * 
	 * This method modifies the advance payment amount of the order based on whether the action ID indicates an addition or deduction.
	 * The advance payment amount is updated by adding or subtracting the specified amount, depending on the action ID.
	 * 
	 * @param actionId the action to perform (0 for addition, non-zero for subtraction)
	 * @param amount the amount to add or subtract from the advance payment
	 * @param orderId the ID of the order to update the advance payment for
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE order_quotation oq "
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
	 * If the order has a quotation, the advance payment amount is returned as 0.
	 * 
	 * @param orderId the ID of the order to retrieve the advance payment for
	 * @return the total advance payment amount for the order
	 */
	@Query(value = "SELECT "
			+ "CASE "
			+ "WHEN :orderId IN (SELECT fk_customer_order_details_id FROM order_quotation) THEN 0 "
			+ "ELSE "
			+ "COALESCE((SELECT SUM(cprd.amount) FROM cash_payment_receipt_details cprd WHERE cprd.voucher_number = :orderId AND cprd.voucher_type = 7), 0) "
			+ "+ "
			+ "COALESCE((SELECT SUM(bprd.amount) FROM bank_payment_receipt_details bprd WHERE bprd.voucher_number = :orderId AND bprd.voucher_type = 7), 0) "
			+ "END AS total_amount;", nativeQuery = true)
	double getAdvancePayment(@Param("orderId") Long orderId);

}