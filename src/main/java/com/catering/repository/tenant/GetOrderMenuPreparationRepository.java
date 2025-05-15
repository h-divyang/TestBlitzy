package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.GetMenuPreparationModel;

/**
 * GetOrderMenuPreparationRepository is an interface for performing data access operations
 * on the GetMenuPreparationModel entity.
 *
 * This repository provides methods for retrieving data related to menu preparation for
 * specific orders, utilizing the inherited CRUD methods from JpaRepository as well as
 * custom query methods defined here.
 */
public interface GetOrderMenuPreparationRepository extends JpaRepository<GetMenuPreparationModel, Long> {

	/**
	 * Retrieves a list of GetMenuPreparationModel entities associated with a given order ID.
	 *
	 * @param orderId The ID of the order for which the menu preparation models need to be retrieved.
	 * @return A list of GetMenuPreparationModel entities corresponding to the specified order ID.
	 */
	List<GetMenuPreparationModel> findByOrderFunctionBookOrderId(Long orderId);

}