package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CommonMultiLanguageDto;
import com.catering.exception.RestException;
import com.catering.service.tenant.OrderStatusService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The {@link OrderStatusController} is responsible for managing order statuses in the system.
 * It provides endpoints to retrieve and manipulate the statuses of orders. 
 * This controller serves as the interface between the frontend and the backend, 
 * allowing users to view and manage the current status of orders, particularly focusing on active statuses.
 * 
 * <p>This controller includes:</p>
 * <ul>
 *   <li>Fetching active order statuses to be displayed on the dashboard or for ongoing orders.</li>
 * </ul>
 * 
 * <p>Each endpoint ensures that only authorized users with the appropriate rights (view, edit, add) 
 * can perform actions related to order statuses.</p>
 * 
 * <p>Endpoints:</p>
 * <ul>
 *   <li><b>GET /order-status/active</b> - Retrieves a list of active order statuses.</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@Tag(name = SwaggerConstant.ORDER_STATUS)
@RequestMapping(value = ApiPathConstant.ORDER_STATUS)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderStatusController {

	/**
	 * Service to handle all business logic related to order status retrieval and manipulation.
	 */
	OrderStatusService orderStatusService;

	/**
	 * Retrieves the list of active order statuses.
	 * This method fetches order status information where the `isActive` field is `true`. It’s typically used to display
	 * active statuses in the user interface, such as on the dashboard or for ongoing orders.
	 * 
	 * @return A {@link ResponseContainerDto} containing a list of {@link OrderStatusDto}, representing the active order statuses.
	 * @throws RestException if there’s an issue fetching the active order statuses from the database.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DASHBOARD + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<CommonMultiLanguageDto>> getIsActive() {
		return RequestResponseUtils.generateResponseDto(orderStatusService.findByIsActiveTrue());
	}

}