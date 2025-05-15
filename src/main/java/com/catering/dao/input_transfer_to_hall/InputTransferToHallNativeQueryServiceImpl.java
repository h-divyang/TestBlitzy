package com.catering.dao.input_transfer_to_hall;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.InputTransferToHallCalculationDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.repository.tenant.BookOrderRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link InputTransferToHallNativeQueryService} interface.
 * This service handles the business logic for input transfer to hall operations, 
 * interacting with the data layer through the {@link InputTransferToHallNativeQueryDao}.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class InputTransferToHallNativeQueryServiceImpl implements InputTransferToHallNativeQueryService {

	/**
	 * DAO instance for handling data access operations related to input transfer to hall.
	 */
	InputTransferToHallNativeQueryDao inputTransferToHallNativeQueryDao;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallUpcomingOrderDto> getUpcomingOrdersForInputTransferToHall(Long orderId) {
		return inputTransferToHallNativeQueryDao.getUpcomingOrdersForInputTransferToHall(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallUpcomingOrderRawMaterial> findInputTransferToHallRawMaterialByOrderId(Long orderId) {
		return inputTransferToHallNativeQueryDao.findInputTransferToHallRawMaterialByOrderId(orderId, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputTransferToHallCalculationDto getInputTransferToHallCalculation(Long id) {
		return inputTransferToHallNativeQueryDao.getInputTransferToHallCalculation(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallRawMaterialDropDownDto> getRawMaterial() {
		return inputTransferToHallNativeQueryDao.getRawMaterial();
	}

}