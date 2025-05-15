package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import com.catering.model.tenant.PurchaseBillModel;

/**
 * Repository interface for accessing and manipulating purchase bill data.
 * 
 * This interface extends `JpaRepository`, providing built-in CRUD operations for `PurchaseBillModel`.
 * It also includes a custom stored procedure `manageTotalBalanceOnPurchaseBill` to calculate the account balance
 * related to a purchase bill based on specific actions.
 */
public interface PurchaseBillRepository extends JpaRepository<PurchaseBillModel, Long> {

	/**
	 * Calls the stored procedure to manage the total balance on a purchase bill.
	 * 
	 * This method calculates the account balance based on the provided action ID and purchase bill ID.
	 * 
	 * @param actionId the ID of the action to be applied to the balance calculation
	 * @param id the ID of the purchase bill for which the balance is being calculated
	 */
	@Procedure(procedureName = "manageTotalBalanceOnPurchaseBill")
	void calculateAccountBalance(Long actionId, Long id);

}