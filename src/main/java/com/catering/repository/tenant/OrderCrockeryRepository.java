package com.catering.repository.tenant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.OrderCrockeryModel;

/**
 * Repository interface for managing {@link OrderCrockeryModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderCrockeryModel}.
 * It includes custom methods for deleting unused crockery based on order function IDs, and for checking the existence of
 * crockery associated with a specific godown (warehouse).
 */
public interface OrderCrockeryRepository extends JpaRepository<OrderCrockeryModel, Long> {

	/**
	 * Deletes unused crockery based on order function IDs that are not included in the provided list.
	 * 
	 * @param orderFunctionId the list of order function IDs to retain, the others will be deleted
	 */
	@Modifying
	@Transactional
	@Query("DELETE FROM OrderCrockeryModel oc "
			+ "WHERE oc.orderFunction NOT IN (:orderFunctionId)")
	void deleteUnusedCrockery(@Param("orderFunctionId") List<Long> orderFunctionId);

	/**
	 * Checks if an {@link OrderCrockeryModel} exists with the specified godown ID.
	 * 
	 * @param id the godown ID to check for
	 * @return {@code true} if a crockery exists with the given godown ID, otherwise {@code false}
	 */
	boolean existsByGodown(Long id);

}