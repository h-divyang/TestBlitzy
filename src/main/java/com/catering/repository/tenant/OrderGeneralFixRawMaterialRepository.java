package com.catering.repository.tenant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.OrderGeneralFixRawMaterialModel;

/**
 * Repository interface for managing {@link OrderGeneralFixRawMaterialModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderGeneralFixRawMaterialModel}.
 * It includes custom methods for deleting unused raw materials based on order function IDs, and for checking the existence of
 * raw materials associated with a specific godown (warehouse).
 */
public interface OrderGeneralFixRawMaterialRepository extends JpaRepository<OrderGeneralFixRawMaterialModel, Long> {

	/**
	 * Deletes unused general fix raw materials based on order function IDs that are not included in the provided list.
	 * The raw materials are also filtered by the presence of their corresponding order function ID in an existing order.
	 * 
	 * @param orderFunctionId the list of order function IDs to retain, the others will be deleted
	 */
	@Modifying
	@Transactional
	@Query("DELETE FROM OrderGeneralFixRawMaterialModel ogfrm "
			+ "WHERE ogfrm.orderFunctionId NOT IN (:orderFunctionId) "
			+ "AND ogfrm.orderFunctionId IN ( "
			+ "SELECT ofun.id  "
			+ "FROM OrderFunctionModel ofun "
			+ "INNER JOIN BookOrderModel bo ON ofun.bookOrder.id = bo.id) ")
	void deleteUnusedGeneralFixRawMaterial(@Param("orderFunctionId") List<Long> orderFunctionId);

	/**
	 * Checks if an {@link OrderGeneralFixRawMaterialModel} exists with the specified godown ID.
	 * 
	 * @param id the godown ID to check for
	 * @return {@code true} if a general fix raw material exists with the given godown ID, otherwise {@code false}
	 */
	boolean existsByGodown(Long id);

}