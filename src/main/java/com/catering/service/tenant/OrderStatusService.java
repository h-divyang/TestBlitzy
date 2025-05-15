package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.request.CommonMultiLanguageDto;

/**
 * Interface for managing order status operations.
 * This service provides a method to retrieve a list of active order statuses.
 */
public interface OrderStatusService {

	/**
	 * Retrieves a list of all active order statuses.
	 *
	 * @return A list of OrderStatusDto representing active order statuses.
	 */
	List<CommonMultiLanguageDto> findByIsActiveTrue();

}