package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.OrderStatusModel;

/**
 * Repository interface for managing {@link OrderStatusModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderStatusModel}.
 * Additionally, it implements {@link CommonNameExistenceRepository} to provide common methods related to checking name existence.
 * It includes a custom method to retrieve all active order statuses.
 */
public interface OrderStatusRepository extends JpaRepository<OrderStatusModel, Long>, CommonNameExistenceRepository {

	/**
	 * Finds all order statuses that are active.
	 * 
	 * This method retrieves all {@link OrderStatusModel} entities that have the {@code isActive} field set to {@code true}.
	 * It is useful for fetching only the order statuses that are currently active in the system.
	 * 
	 * @return a list of active {@link OrderStatusModel} entities
	 */
	List<OrderStatusModel> findByIsActiveTrue();

}