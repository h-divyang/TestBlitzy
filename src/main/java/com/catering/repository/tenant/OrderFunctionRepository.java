package com.catering.repository.tenant;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.catering.model.tenant.OrderFunctionModel;

/**
 * Repository interface for performing CRUD operations and custom queries on the OrderFunctionModel entity.
 * Extends JpaRepository to inherit standard data access methods.
 */
public interface OrderFunctionRepository extends JpaRepository<OrderFunctionModel, Long> {

	/**
	 * Checks the existence of a record in the data store based on the provided function type ID.
	 * This method determines if any entity exists with the specified ID.
	 *
	 * @param id The unique identifier of the function type to check.
	 * @return True if an entity exists with the given function type ID, false otherwise.
	 */
	boolean existsByFunctionTypeId(Long id);

	/**
	 * Updates the rate of an order function identified by its unique ID.
	 *
	 * This method executes a custom query to update the rate of a specific
	 * OrderFunctionModel in the database. It is transactional and modifies existing data.
	 *
	 * @param rate The new rate value to set for the order function.
	 * @param id The unique identifier of the order function to be updated.
	 * @return The number of affected rows in the database.
	 */
	@Modifying
	@Transactional
	@Query("UPDATE OrderFunctionModel ofm SET ofm.rate = :rate WHERE ofm.id = :id")
	int updateRate(Double rate, Long id);

}