package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.catering.dto.tenant.request.CommonNotesDto;
import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.OrderLabourDistributionDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.OrderLabourDistributionModel;
import com.catering.repository.tenant.OrderLabourDistributionRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.OrderLabourDistributionService;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the OrderLabourDistributionService interface, which provides methods
 * to manage labour distribution data associated with orders. This service facilitates
 * operations for creating, updating, retrieving, deleting, and validating such data
 * as well as updating associated notes.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderLabourDistributionServiceimpl implements OrderLabourDistributionService {

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing the distribution of labour in orders.
	 */
	OrderLabourDistributionRepository orderLabourDistributionRepository;

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * {@inheritDoc}
	 * See {@link OrderLabourDistributionService#createAndUpdate(List)} for details.
	 */
	@Override
	public List<OrderLabourDistributionDto> createAndUpdate(List<OrderLabourDistributionDto> orderLabourDistributionDtos) {
		List<OrderLabourDistributionModel> orderLabourDistributionModels = new ArrayList<>();
		orderLabourDistributionDtos.forEach(orderLabourDistribution -> {
			OrderLabourDistributionModel orderLabourDistributionModel = modelMapperService.convertEntityAndDto(orderLabourDistribution, OrderLabourDistributionModel.class);
			orderLabourDistributionModel.setGodown(orderLabourDistribution.getGodown() == null || orderLabourDistribution.getGodown().getId() == null ? null : orderLabourDistributionModel.getGodown());
			DataUtils.setAuditFields(orderLabourDistributionRepository, orderLabourDistribution.getId(), orderLabourDistributionModel);
			orderLabourDistributionModels.add(orderLabourDistributionRepository.save(orderLabourDistributionModel));
		});
		return modelMapperService.convertListEntityAndListDto(orderLabourDistributionModels, OrderLabourDistributionDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<OrderLabourDistributionDto>> findByOrderFunctionId(Long orderFunctionId) {
		List<OrderLabourDistributionModel> orderMenuPreparationMenuItemModels = orderLabourDistributionRepository.findByOrderFunctionId(orderFunctionId);
		return Optional.of(modelMapperService.convertListEntityAndListDto(orderMenuPreparationMenuItemModels, OrderLabourDistributionDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!orderLabourDistributionRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		orderLabourDistributionRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByGodownId(Long id) {
		return orderLabourDistributionRepository.existsByGodown_Id(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNotes(CommonNotesDto commonNotesDto) {
		if (!orderLabourDistributionRepository.existsById(commonNotesDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<OrderLabourDistributionModel> orderLabourDistributionModel = orderLabourDistributionRepository.findById(commonNotesDto.getId());
		if (orderLabourDistributionModel.isPresent()) {
			OrderLabourDistributionModel orderLabourDistribution = orderLabourDistributionModel.get();
			orderLabourDistribution.setNoteDefaultLang(commonNotesDto.getNoteDefaultLang());
			orderLabourDistribution.setNotePreferLang(commonNotesDto.getNotePreferLang());
			orderLabourDistribution.setNoteSupportiveLang(commonNotesDto.getNoteSupportiveLang());
			orderLabourDistributionRepository.save(orderLabourDistribution);
		}
	}

}