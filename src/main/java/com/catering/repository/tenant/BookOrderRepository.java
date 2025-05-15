package com.catering.repository.tenant;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CommonMultiLanguageDto;
import com.catering.model.tenant.BookOrderModel;

/**
 * BookOrderRepository is an interface responsible for performing CRUD operations
 * and custom queries related to the {@code BookOrderModel} entity.
 *
 * This repository extends {@code JpaRepository}, which provides methods to interact
 * with the database via standard JPA functionality.
 *
 * It includes custom-defined operations for querying and modifying data related
 * to book orders, meal types, event types, menu preparation, labour distribution,
 * invoices, and order statuses.
 */
public interface BookOrderRepository extends JpaRepository<BookOrderModel, Long> {

	/**
	 * Retrieves a list of upcoming book orders with event dates scheduled on or after the current date.
	 * The results are ordered by the event date in ascending order.
	 *
	 * @return A list of {@code BookOrderModel} objects representing the upcoming orders.
	 */
	@Query("SELECT DISTINCT bom FROM BookOrderModel bom JOIN bom.functions of2 WHERE bom.eventMainDate >= CURRENT_DATE OR of2.date >= CURRENT_DATE() OR of2.endDate >= CURRENT_DATE() ORDER BY bom.eventMainDate")
	List<BookOrderModel> findUpcomingOrders();

	/**
	 * Checks if a meal type with the specified ID exists in the database.
	 *
	 * @param id The unique identifier of the meal type to check for existence.
	 * @return True if a meal type with the given ID exists, otherwise false.
	 */
	boolean existsByMealTypeId(Long id);

	/**
	 * Checks if an event type with the specified ID exists in the database.
	 *
	 * @param id The unique identifier of the event type to check for existence.
	 * @return True if an event type with the given ID exists, otherwise false.
	 */
	boolean existsByEventTypeId(Long id);

	/**
	 * Checks if the menu is prepared for a specific book order identified by its ID.
	 * This method executes a query to determine whether there is a menu preparation associated
	 * with the specified book order by evaluating related entities in the database.
	 *
	 * @param id The unique identifier of the book order for which to check the menu preparation status.
	 * @return True if the menu is prepared for the provided book order ID, otherwise false.
	 */
	@Query(value = "SELECT "
		+ "COUNT(bom.id) > 0 "
		+ "FROM BookOrderModel bom "
		+ "INNER JOIN OrderFunctionModel ofm ON ofm.bookOrder.id = bom.id "
		+ "RIGHT JOIN OrderMenuPreparationModel ompm ON ompm.orderFunction.id = ofm.id "
		+ "RIGHT JOIN OrderMenuPreparationMenuItemModel ompmi ON ompmi.menuPreparation.id = ompm.id "
		+ "WHERE bom.id = :id")
	Boolean isMenuPrepared(Long id);

	/**
	 * Deletes menu allocation type records from the database associated with a specific book order ID.
	 * This method removes entries where the referenced menu preparation menu item exists
	 * within a hierarchical query that links the book order with its associated menu preparation data.
	 *
	 * @param id The unique identifier of the book order for which menu allocation types need to be deleted.
	 */
	@Modifying
	@Query("DELETE FROM GetMenuAllocationTypeModel omat "
		+ "WHERE omat.menuPreparationMenuItem IN ( "
		+ " SELECT ompmi.id "
		+ " FROM OrderMenuPreparationMenuItemModel ompmi "
		+ " JOIN ompmi.menuPreparation omp "
		+ " JOIN omp.orderFunction ofm "
		+ " JOIN ofm.bookOrder bom "
		+ " WHERE bom.id = :id"
		+ ")")
	void deleteMenuItem(Long id);

	/**
	 * Deletes labour distribution records associated with a specific book order ID.
	 * This operation removes entries in the `OrderLabourDistributionModel` table
	 * where the corresponding `orderFunction.id` matches the IDs of functions
	 * that belong to the given book order.
	 *
	 * @param id The unique identifier of the book order whose labour distribution records need to be deleted.
	 */
	@Modifying
	@Query(value = "DELETE FROM OrderLabourDistributionModel old WHERE old.orderFunction.id IN ("
		+ "SELECT ofn.id FROM OrderFunctionModel ofn WHERE ofn.bookOrder.id = :id)")
	void deleteLabour(Long id);

	/**
	 * Deletes an invoice associated with a specific order ID.
	 * This method performs a delete operation in the database to remove the `OrderInvoiceModel`
	 * record that matches the provided order ID.
	 *
	 * @param id The unique identifier of the order for which the invoice needs to be deleted.
	 */
	@Modifying
	@Query(value = "DELETE FROM OrderInvoiceModel ov WHERE ov.orderId = :id")
	void deleteInvoice(Long id);

	/**
	 * Updates the status of a customer order in the database.
	 * This method modifies the `fk_order_status_id` field of a record in the
	 * `customer_order_details` table based on the provided order ID and order status details.
	 *
	 * @param orderStatusDto The data transfer object containing the status details to be applied to the specified order.
	 * @param orderId The unique identifier of the customer order whose status needs to be updated.
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE customer_order_details cod SET "
		+ "cod.fk_order_status_id  = :#{#orderStatusDto.id} "
		+ "WHERE cod.id  = :orderId", nativeQuery = true)
	void updateStatus(CommonMultiLanguageDto orderStatusDto, Long orderId);

	/**
	 * Retrieves the 'Adjust Quantity' setting for a specific order by its ID.
	 *
	 * @param orderId the ID of the order.
	 * @return a Boolean indicating whether the 'Adjust Quantity' setting is enabled for the order.
	 */
	@Query(value = "SELECT bo.isAdjustQuantity FROM BookOrderModel bo WHERE bo.id = :orderId")
	Boolean getAdjustQuantityByOrderId(Long orderId);

	/**
	 * Retrieves the 'Adjust Quantity' setting of the order associated with a specific function by its ID.
	 *
	 * @param functionId the ID of the function.
	 * @return a Boolean indicating whether the 'Adjust Quantity' setting is enabled for the associated order.
	 */
	@Query("SELECT o.bookOrder.isAdjustQuantity FROM OrderFunctionModel o WHERE o.id = :functionId")
	Boolean getAdjustQuantityOfOrderByFunctionId(Long functionId);

	/**
	 * Retrieves the ID of the most recently created order.
	 *
	 * This method uses a native SQL query to fetch the latest order based on the
	 * highest ID value, assuming that order IDs are auto-incremented.
	 *
	 * @return the ID of the latest order as a Long value
	 */
	@Query(value = "SELECT cod.id FROM customer_order_details cod ORDER BY cod.id DESC LIMIT 1", nativeQuery = true)
	Long getLastestOrderId();

}