package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.PaymentTransactionModel;

/**
 * Repository interface for managing {@link PaymentTransactionModel} entities.
 * 
 * This interface extends the {@link JpaRepository} to provide standard CRUD operations on {@link PaymentTransactionModel}.
 * It includes a custom method to retrieve a list of payment transactions ordered by their ID in descending order.
 */
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionModel, Long> {

	/**
	 * Finds all payment transactions ordered by their ID in descending order.
	 * 
	 * This method retrieves all {@link PaymentTransactionModel} entities, sorted by their ID in descending order.
	 * The most recent payment transaction (based on ID) will be retrieved first.
	 * 
	 * @return a list of {@link PaymentTransactionModel} entities, ordered by ID in descending order
	 */
	List<PaymentTransactionModel> findByOrderByIdDesc();

}