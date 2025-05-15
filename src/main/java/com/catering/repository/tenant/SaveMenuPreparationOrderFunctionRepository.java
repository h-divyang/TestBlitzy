package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.SaveMenuPreparationOrderFunctionModel;

/**
 * Repository interface for accessing and manipulating data related to menu preparation order functions.
 * 
 * This interface extends `JpaRepository` to provide methods for querying and managing the `SaveMenuPreparationOrderFunctionModel` entities.
 */
public interface SaveMenuPreparationOrderFunctionRepository extends JpaRepository<SaveMenuPreparationOrderFunctionModel, Long> {

	/**
	 * Retrieves the order function IDs where the person count is the maximum for each order.
	 * 
	 * This query returns a list of order function IDs, where each ID corresponds to the record with the highest person count (`person`) 
	 * for a given `orderId`. The query groups the results by `orderId` and selects the function ID of the record with the maximum 
	 * `person` value for each order.
	 * 
	 * @return a list of order function IDs with the maximum person count for each order
	 */
	@Query("SELECT of2.id "
			+ "FROM SaveMenuPreparationOrderFunctionModel of2 "
			+ "WHERE of2.person = (SELECT MAX(of3.person) "
			+ "FROM SaveMenuPreparationOrderFunctionModel of3 "
			+ "WHERE of2.orderId = of3.orderId) "
			+ "GROUP BY of2.orderId ")
	List<Long> retrieveMaxPersonOrderFunctionIds();

}