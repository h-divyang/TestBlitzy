package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.GetMenuPreparationOrderFunctionModel;

/**
 * GetMenuPreparationOrderFunctionRepository is an interface for performing data access operations
 * on the GetMenuPreparationOrderFunctionModel entity.
 *
 * This repository provides methods for retrieving order function details associated with
 * specific orders. It leverages inherited CRUD functionality from JpaRepository and includes
 * custom query methods for specialized data retrieval.
 */
public interface GetMenuPreparationOrderFunctionRepository extends JpaRepository<GetMenuPreparationOrderFunctionModel, Long> {

	/**
	 * Retrieves all order function entries associated with the specified order ID.
	 *
	 * @param orderId the ID of the order whose function entries are to be retrieved
	 * @return a list of GetMenuPreparationOrderFunctionModel entities linked to the order
	 */
	@Query(value = "SELECT ofun FROM GetMenuPreparationOrderFunctionModel ofun WHERE ofun.bookOrderId = :orderId")
	List<GetMenuPreparationOrderFunctionModel> findOrderFunctionsOfLastestOrder(Long orderId);

}