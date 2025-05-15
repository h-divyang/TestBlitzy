package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.OrderLabourDistributionModel;

/**
 * Repository interface for managing {@link OrderLabourDistributionModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderLabourDistributionModel}.
 * It includes custom methods to retrieve records based on order function ID and to check the existence of records based on godown ID.
 */
public interface OrderLabourDistributionRepository extends JpaRepository<OrderLabourDistributionModel, Long> {

	/**
	 * Finds a list of {@link OrderLabourDistributionModel} entities associated with a specific order function ID.
	 * 
	 * @param orderFunctionId the order function ID to filter by
	 * @return a list of {@link OrderLabourDistributionModel} entities associated with the specified order function ID
	 */
	List<OrderLabourDistributionModel> findByOrderFunctionId(Long orderFunctionId);

	/**
	 * Checks if any {@link OrderLabourDistributionModel} exists with a specific godown ID.
	 * 
	 * @param id the godown ID to check for
	 * @return {@code true} if a record with the given godown ID exists, otherwise {@code false}
	 */
	boolean existsByGodown_Id(Long id);

}