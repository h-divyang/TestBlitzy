package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.GetMenuAllocationOrderMenuPreparationModel;

/**
 * Repository interface for managing data access related to
 * {@code GetMenuAllocationOrderMenuPreparationModel}.
 * This interface provides methods to interact with the underlying database
 * for operations related to menu preparation entities in an order allocation context.
 *
 * It extends the {@code JpaRepository} interface to inherit basic CRUD operations
 * and allows for the creation of custom query methods.
 */
public interface GetAllocationOrderMenuPreparationRepository extends JpaRepository<GetMenuAllocationOrderMenuPreparationModel, Long> {

	/**
	 * Retrieves a list of {@code GetMenuAllocationOrderMenuPreparationModel} entities
	 * associated with a specific order based on the provided order ID and its corresponding
	 * order function book order mapping.
	 *
	 * @param orderId The unique identifier of the order for which to retrieve the menu preparation details.
	 * @return A list of {@code GetMenuAllocationOrderMenuPreparationModel} objects representing
	 *		   the menu preparation details linked to the specified order. If no such records
	 *		   are found, an empty list is returned.
	 */
	List<GetMenuAllocationOrderMenuPreparationModel> findByOrderFunctionBookOrder(Long orderId);

}