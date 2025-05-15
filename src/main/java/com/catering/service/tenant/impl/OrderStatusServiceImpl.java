package com.catering.service.tenant.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CommonMultiLanguageDto;
import com.catering.repository.tenant.OrderStatusRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.OrderStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the OrderStatusService interface.
 * This service is responsible for managing and retrieving order status information.
 * It provides functionality to retrieve active order statuses and convert them to DTOs.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderStatusServiceImpl implements OrderStatusService {

	/**
	 * Repository for managing order status data.
	 */
	OrderStatusRepository orderStatusRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CommonMultiLanguageDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(orderStatusRepository.findByIsActiveTrue(), CommonMultiLanguageDto.class);
	}

}