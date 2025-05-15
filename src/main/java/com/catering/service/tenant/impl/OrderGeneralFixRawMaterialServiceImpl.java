package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialDto;
import com.catering.model.tenant.OrderGeneralFixRawMaterialModel;
import com.catering.repository.tenant.OrderGeneralFixRawMaterialRepository;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.OrderGeneralFixRawMaterialService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This service implementation provides methods for managing Order General Fix Raw Material entities,
 * including saving, deleting, and checking their existence.
 * It extends the generic service implementation {@link GenericServiceImpl} to handle CRUD operations.
 * This class implements {@link OrderGeneralFixRawMaterialService} to define the specific business logic
 * for handling raw materials related to general order fixes.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderGeneralFixRawMaterialServiceImpl extends GenericServiceImpl<OrderGeneralFixRawMaterialDto, OrderGeneralFixRawMaterialModel, Long> implements OrderGeneralFixRawMaterialService {

	/**
	 * Repository for managing raw materials related to general order fixes.
	 */
	OrderGeneralFixRawMaterialRepository orderGeneralFixRawMaterialRepository;

	/**
	 * {@inheritDoc}
	 * See {@link OrderGeneralFixRawMaterialService#saveOrderGeneralFixRawMaterial(List)} for details.
	 */
	@Override
	public void saveOrderGeneralFixRawMaterial(List<OrderGeneralFixRawMaterialDto> orderGeneralFixRawMaterialDtos) {
		validate(orderGeneralFixRawMaterialDtos);
		orderGeneralFixRawMaterialDtos.forEach(orderGeneralFixRawMaterialDto -> createAndUpdate(orderGeneralFixRawMaterialDto, OrderGeneralFixRawMaterialDto.class, OrderGeneralFixRawMaterialModel.class, orderGeneralFixRawMaterialDto.getId()));
	}

	/**
	 * {@inheritDoc}
	 * See {@link OrderGeneralFixRawMaterialService#deleteUnusedGeneralFixRawMaterial(List)} for details.
	 */
	@Override
	public void deleteUnusedGeneralFixRawMaterial(List<Long> orderFunctionId) {
		orderGeneralFixRawMaterialRepository.deleteUnusedGeneralFixRawMaterial(orderFunctionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByGodownId(Long id) {
		return orderGeneralFixRawMaterialRepository.existsByGodown(id);
	}

	/**
	 * Validates a list of OrderGeneralFixRawMaterialDto objects, ensuring that
	 * the quantity (qty) is not null or negative. If the quantity is null or negative, it defaults the quantity to 0.
	 *
	 * @param orderGeneralFixRawMaterialDtos The list of OrderGeneralFixRawMaterialDto objects to validate;
	 *										 can be null or empty, in which case no action is taken
	 */
	private void validate(List<OrderGeneralFixRawMaterialDto> orderGeneralFixRawMaterialDtos) {
		if (Objects.nonNull(orderGeneralFixRawMaterialDtos)) {
			orderGeneralFixRawMaterialDtos.forEach(orderGeneralFixRawMaterialDto -> {
				if (Objects.isNull(orderGeneralFixRawMaterialDto.getQty()) || orderGeneralFixRawMaterialDto.getQty() < 0) {
					orderGeneralFixRawMaterialDto.setQty(0d);
				}
			});
		}
	}

}