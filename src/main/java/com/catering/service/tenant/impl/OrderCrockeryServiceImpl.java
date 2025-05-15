package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.OrderCrockeryDto;
import com.catering.model.tenant.OrderCrockeryModel;
import com.catering.repository.tenant.OrderCrockeryRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.OrderCrockeryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link OrderCrockeryService} interface for managing crockery order operations.
 *
 * This class extends {@link GenericServiceImpl} to provide commonly used CRUD methods
 * along with additional functionality specifically for managing crockery orders.
 * It performs validation, persistence, and other operations related to the {@link OrderCrockeryDto} and {@link OrderCrockeryModel}.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCrockeryServiceImpl extends GenericServiceImpl<OrderCrockeryDto, OrderCrockeryModel, Long> implements OrderCrockeryService {

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing order crockery data.
	 */
	OrderCrockeryRepository orderCrockeryRepository;

	/**
	 * {@inheritDoc}
	 * See {@link OrderCrockeryService#saveCrockeryData(List)} for details.
	 */
	@Override
	public void saveCrockeryData(List<OrderCrockeryDto> orderCrockeries) {
		validate(orderCrockeries);
		orderCrockeries.forEach(orderCrockery -> createAndUpdate(orderCrockery, OrderCrockeryDto.class, OrderCrockeryModel.class, orderCrockery.getId()));
	}

	/**
	 * {@inheritDoc}
	 * See {@link OrderCrockeryService#deleteCrockeryData(List)} for details.
	 */
	@Override
	public void deleteCrockeryData(List<Long> orderFunctionId) {
		orderCrockeryRepository.deleteUnusedCrockery(orderFunctionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByGodownId(Long id) {
		return orderCrockeryRepository.existsByGodown(id);
	}

	/**
	 * Validates a list of OrderCrockeryDto objects to ensure that required fields are present
	 * and numeric values meet the expected criteria. Any invalid data will either be corrected
	 * or result in a bad request exception being thrown.
	 *
	 * @param orderCrockeries List of OrderCrockeryDto objects to be validated. Each object must have non-null rawMaterialId and orderFunction,
	 *						  and a non-negative qty. If qty is null or negative, it will be set to 0.
	 *						  If rawMaterialId or orderFunction is null, a bad request exception is thrown.
	 */
	private void validate(List<OrderCrockeryDto> orderCrockeries) {
		if (Objects.nonNull(orderCrockeries)) {
			orderCrockeries.forEach(orderCrockery -> {
				if (Objects.isNull(orderCrockery.getQty()) || orderCrockery.getQty() < 0) {
					orderCrockery.setQty(0d);
				}
				if (Objects.isNull(orderCrockery.getRawMaterialId()) || Objects.isNull(orderCrockery.getOrderFunction())) {
					exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT));
				}
			});
		}
	}

}