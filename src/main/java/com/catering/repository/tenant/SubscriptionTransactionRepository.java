package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.SubscriptionTransactionModel;

/**
 * Repository interface for accessing and manipulating subscription transaction data.
 * 
 * This interface extends `JpaRepository` to provide methods for querying and managing subscription transactions.
 */
public interface SubscriptionTransactionRepository extends JpaRepository<SubscriptionTransactionModel, Long> {

	/**
	 * Retrieves all subscription transactions ordered by their ID in descending order.
	 * 
	 * @return a list of `SubscriptionTransactionModel` objects sorted by ID in descending order
	 */
	List<SubscriptionTransactionModel> findAllByOrderByIdDesc();

}