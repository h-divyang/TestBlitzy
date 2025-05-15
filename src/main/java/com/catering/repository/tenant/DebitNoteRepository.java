package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import com.catering.model.tenant.DebitNoteModel;

/**
 * DebitNoteRepository is an interface for managing DebitNoteModel entities,
 * providing a method to perform operations using a stored procedure and basic
 * CRUD operations through the JpaRepository interface.
 *
 * This repository acts as the data access layer for DebitNoteModel, enabling
 * seamless interaction with the database and facilitating custom actions
 * such as calculating account balances.
 */
public interface DebitNoteRepository extends JpaRepository<DebitNoteModel, Long> {

	/**
	 * Executes a stored procedure to manage the total balance associated with a debit note.
	 *
	 * @param actionId The unique identifier of the action to be performed.
	 * @param id The unique identifier of the debit note for which the account balance is to be calculated.
	 */
	@Procedure(procedureName = "manageTotalBalanceOnDebitNote")
	void calculateAccountBalance(Long actionId, Long id);

}