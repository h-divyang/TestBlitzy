package com.catering.controller.tenant;

import java.util.List;
import javax.validation.Valid;
import com.catering.dto.tenant.request.*;
import org.springframework.web.bind.annotation.*;
import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.order.BookOrderNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.BookOrderService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing book orders.
 *
 * This controller provides API end points for creating, retrieving, updating,
 * and deleting book orders. It also includes end points for specific operations
 * such as retrieving upcoming orders, dashboard-related order data, dish costing
 * calculations, and updating the status of a book order.
 */
@RestController
@RequestMapping(value = ApiPathConstant.BOOK_ORDER)
@Tag(name = SwaggerConstant.BOOK_ORDER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookOrderController {

	/**
	 * Service for managing and processing book orders.
	 */
	BookOrderService bookOrderService;

	/**
	 * Service for handling messages or notifications within the application.
	 */
	MessageService messageService;

	/**
	 * Service for executing native queries related to book orders.
	 */
	BookOrderNativeQueryService bookOrderNativeQueryService;

	/**
	 * Creates a new book order based on the provided details.
	 *
	 * @param bookOrderDto The book order data transfer object containing the details of the book order to be created. This parameter must be validated and not null.
	 * @return A ResponseContainerDto containing the created BookOrderDto and a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<BookOrderDto> create(@Valid @RequestBody BookOrderDto bookOrderDto) {
		BookOrderDto bookOrderResponseDto = bookOrderService.createAndUpdate(bookOrderDto);
		return RequestResponseUtils.generateResponseDto(bookOrderResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of book orders based on the specified filter criteria.
	 *
	 * @param filterDto The filter criteria used to query the list of book orders. This parameter must be a valid instance of {@code FilterDto}.
	 * @return A {@code ResponseContainerDto} containing a list of {@code BookOrderDto} objects that match the filter criteria.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<BookOrderDto>> read(FilterDto filterDto) {
		return bookOrderService.read(filterDto);
	}

	/**
	 * Updates an existing book order using the provided details.
	 *
	 * @param bookOrderDto The book order data transfer object containing the updated details of the book order. This parameter must be validated and not null.
	 * @return A ResponseContainerDto containing the updated BookOrderDto and a success message indicating that the data has been updated successfully.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<BookOrderDto> update(@Valid @RequestBody BookOrderDto bookOrderDto) {
		BookOrderDto bookOrderResponseDto = bookOrderService.createAndUpdate(bookOrderDto);
		return RequestResponseUtils.generateResponseDto(bookOrderResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes the book order associated with the specified ID.
	 *
	 * This method handles the deletion of a book order and returns a response indicating
	 * the success of the operation along with a success message.
	 *
	 * @param id The unique identifier of the book order to be deleted. Must not be null.
	 * @return A {@code ResponseContainerDto} containing {@code null} as the data and a success message indicating that the book order has been successfully deleted.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) {
		bookOrderService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of upcoming book orders for various events.
	 *
	 * @return A {@code ResponseContainerDto} containing a list of {@code OrderForUpcomingEventDto} objects representing upcoming book orders.
	 */
	@GetMapping(value = ApiPathConstant.BOOK_ORDER_UPCOMING)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT, ApiUserRightsConstants.DISH_COSTING + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<OrderForUpcomingEventDto>> upcomingOrders() {
		return RequestResponseUtils.generateResponseDto(bookOrderService.upcomingOrders());
	}

	/**
	 * Retrieves a list of book orders for use in the dash board.
	 *
	 * This method provides an API end point to fetch details of book orders
	 * displayed in the dash board for quick access and management.
	 *
	 * @return A {@code ResponseContainerDto} containing a list of {@code OrderForDashboardDto} objects, representing details of book orders for the dash board.
	 */
	@GetMapping(value = ApiPathConstant.BOOK_ORDER_DETAILS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<OrderForDashboardDto>> getBookOrders() {
		return RequestResponseUtils.generateResponseDto(bookOrderService.getBookOrderForDashboard());
	}

	/**
	 * Retrieves the details of a specific book order by its unique identifier ID.
	 *
	 * This method fetches the book order details that are required for dash board display and management purposes, based on the provided ID.
	 *
	 * @param id The unique identifier of the book order to retrieve. Must not be null.
	 * @return A {@code ResponseContainerDto} containing a {@code BookOrderDto} representing the details
	 *		   of the book order associated with the provided identifier.
	 * @throws RestException if an error occurs while retrieving the book order details.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DASHBOARD + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW,
			ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<BookOrderDto> getBookOrdersById(@PathVariable Long id) throws RestException {
		return RequestResponseUtils.generateResponseDto(bookOrderService.getBookOrderForDashboardById(id));
	}

	/**
	 * Calculates the cost for each dish associated with a specific order.
	 *
	 * This method interacts with the data service to determine the per-dish costing
	 * details for the order identified by its unique ID and returns the results.
	 *
	 * @param orderId The unique identifier of the order for which dish costing is to be calculated. Must not be null.
	 * @return A {@code ResponseContainerDto} containing a list of {@code DishCostingDto} objects
	 *		   representing the costing details for each dish in the specified order.
	 */
	@GetMapping(value = ApiPathConstant.DISH_COSTING)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DISH_COSTING + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DishCostingDto>> perDishCalculation(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(bookOrderNativeQueryService.dishCostingByOrderId(orderId));
	}

	/**
	 * Updates the status of an existing book order.
	 *
	 * @param orderId The unique identifier of the book order whose status is to be updated. Must not be null.
	 * @param orderStatusDto The data transfer object containing the updated status information for the book order. This parameter must be validated and not null.
	 * @return A {@code ResponseContainerDto} containing a list of {@code DishCostingDto} objects and a message indicating the success of the operation.
	 */
	@PutMapping(value = ApiPathConstant.ORDER_STATUS_UPDATE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<List<DishCostingDto>> updateStatus(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @Valid @RequestBody CommonMultiLanguageDto orderStatusDto) {
		bookOrderService.updateStatus(orderStatusDto, orderId);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}