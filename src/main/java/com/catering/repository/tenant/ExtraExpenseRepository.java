package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.ExtraExpenseModel;

/**
 * ExtraExpenseRepository is an interface for managing ExtraExpenseModel entities.
 * It extends JpaRepository to provide basic CRUD (Create, Read, Update, Delete) functionality,
 * and includes additional methods for querying data related to extra expenses.
 *
 * This repository serves as a data access layer for ExtraExpenseModel, enabling
 * interactions with the database to retrieve and manage extra expense records.
 */
public interface ExtraExpenseRepository extends JpaRepository<ExtraExpenseModel, Long> {

	/**
	 * Retrieves a list of ExtraExpenseModel entities associated with the specified order function ID,
	 * sorted according to the provided sorting criteria.
	 *
	 * @param orderFunctionId The ID of the order function to filter the extra expenses.
	 * @param sort The sorting criteria to apply to the result list.
	 * @return A list of ExtraExpenseModel entities matching the specified order function ID and sorted as per the provided criteria.
	 */
	List<ExtraExpenseModel> findByOrderFunctionId(Long orderFunctionId, Sort sort);

	/**
	 * Retrieves a list of ExtraExpenseModel entities associated with a specific
	 * order function, sorted in descending order by ID.
	 *
	 * @param orderMenuPreparationId The ID of the order menu preparation to filter the extra expenses.
	 * @return A list of ExtraExpenseModel entities matching the specified criteria, sorted in descending order by ID.
	 */
	List<ExtraExpenseModel> findByOrderFunctionOrderByIdDesc(Long orderMenuPreparationId);

}