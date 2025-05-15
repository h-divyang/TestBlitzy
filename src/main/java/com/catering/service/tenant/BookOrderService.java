package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.BookOrderDto;
import com.catering.dto.tenant.request.CommonMultiLanguageDto;
import com.catering.dto.tenant.request.OrderForDashboardDto;
import com.catering.dto.tenant.request.OrderForUpcomingEventDto;
import com.catering.model.tenant.GetBookOrderListModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing book orders.
 * Provides operations to create, update, retrieve, check existence, and update statuses of book orders.
 */
public interface BookOrderService extends GenericService<BookOrderDto, GetBookOrderListModel, Long> {

	/**
	 * Creates and updates a book order.
	 *
	 * @param bookOrderDto The data transfer object containing details of the book order to be created or updated.
	 * @return The updated or newly created book order data transfer object.
	 */
	BookOrderDto createAndUpdate(BookOrderDto bookOrderDto);

	/**
	 * Retrieves a list of book orders based on the provided filter criteria.
	 *
	 * @param filterDto A data transfer object containing the filter criteria such as pagination, sorting, and query parameters.
	 * @return A ResponseContainerDto containing a list of BookOrderDto objects that match the specified filter criteria.
	 */
	ResponseContainerDto<List<BookOrderDto>> read(FilterDto filterDto);

	/**
	 * Retrieves a list of orders for upcoming events.
	 *
	 * @return A list of OrderForUpcomingEventDto objects representing orders for upcoming events.
	 */
	List<OrderForUpcomingEventDto> upcomingOrders();

	/**
	 * Retrieves a list of book orders formatted for display on the dash board.
	 *
	 * @return A list of OrderForDashboardDto objects containing details of book orders intended for the dash board view.
	 */
	List<OrderForDashboardDto> getBookOrderForDashboard();

	/**
	 * Retrieves a book order by its unique identifier and formats it for dash board display.
	 *
	 * @param id The unique identifier of the book order to retrieve.
	 * @return A BookOrderDto containing the details of the book order formatted for the dash board view.
	 */
	BookOrderDto getBookOrderForDashboardById(Long id);

	/**
	 * Checks the existence of an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @return True if an entity with the specified identifier exists, otherwise false.
	 */
	boolean existsById(Long id);

	/**
	 * Checks if an order function exists by its unique identifier.
	 *
	 * @param id The unique identifier of the order function to check.
	 * @return True if an order function with the specified identifier exists, otherwise false.
	 */
	boolean existsByOrderFunctionId(Long id);

	/**
	 * Checks if an order function exists based on its associated function type identifier.
	 *
	 * @param id The unique identifier of the function type associated with the order function.
	 * @return True if an order function with the specified function type identifier exists, otherwise false.
	 */
	boolean existsOrderFunctionByFunctionTypeId(Long id);

	/**
	 * Checks if a meal type exists by its unique identifier.
	 *
	 * @param id The unique identifier of the meal type to check.
	 * @return True if a meal type with the specified identifier exists, otherwise false.
	 */
	boolean existsByMealTypeId(Long id);

	/**
	 * Checks if an event type exists by its unique identifier.
	 *
	 * @param id The unique identifier of the event type to check.
	 * @return True if an event type with the specified identifier exists, otherwise false.
	 */
	boolean existsByEventTypeId(Long id);

	/**
	 * Updates the status of an order associated with the given ID.
	 *
	 * @param orderStatusDto A data transfer object containing the updated status details of the order.
	 * @param orderId The unique identifier of the order whose status is to be updated.
	 */
	void updateStatus(CommonMultiLanguageDto orderStatusDto, Long orderId);

}