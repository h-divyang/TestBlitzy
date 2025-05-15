package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.ExtraExpenseDto;
import com.catering.dto.tenant.request.ExtraExpenseParameterDto;
import com.catering.model.tenant.ExtraExpenseModel;
import com.catering.repository.tenant.ExtraExpenseRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.BookOrderService;
import com.catering.service.tenant.ExtraExpenseService;
import com.catering.util.PagingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link ExtraExpenseService} interface for managing ExtraExpense entities
 * and their corresponding DTOs. This class extends {@link GenericServiceImpl} to provide a base
 * implementation for common CRUD operations and adds functionality specific to ExtraExpense entities.
 *
 * The class makes use of various services and repositories for handling data operations,
 * mapping, exception handling, and messaging.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExtraExpenseServiceImpl extends GenericServiceImpl<ExtraExpenseDto, ExtraExpenseModel, Long> implements ExtraExpenseService {

	/**
	 * Repository for managing Extra Expense entities.
	 */
	ExtraExpenseRepository extraExpenseRepository;

	/**
	 * Service for mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for handling operations related to Book Orders.
	 */
	BookOrderService bookOrderService;

	/**
	 * Service for handling and throwing application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for managing and retrieving localized messages.
	 */
	MessageService messageService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ExtraExpenseDto> findByOrderFunctionId(ExtraExpenseParameterDto parameterDto) {
		parameterDto.setSortBy(PagingUtils.getDefaultSortingField(parameterDto.getSortBy()));
		parameterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(parameterDto.getSortDirection()));
		Optional<Sort> sort = PagingUtils.sortOf(parameterDto.getSortDirection(), parameterDto.getSortBy(), ExtraExpenseModel.class);
		List<ExtraExpenseModel> list = null;
		if (sort.isPresent()) {
			list = extraExpenseRepository.findByOrderFunctionId(parameterDto.getOrderFunctionId(), sort.get());
		} else {
			list = extraExpenseRepository.findByOrderFunctionOrderByIdDesc(parameterDto.getOrderFunctionId());
		}
		return modelMapperService.convertListEntityAndListDto(list, ExtraExpenseDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExtraExpenseDto createAndUpdate(ExtraExpenseDto extraExpenseDto) {
		if (Objects.isNull(extraExpenseDto.getOrderFunction()) || !bookOrderService.existsByOrderFunctionId(extraExpenseDto.getOrderFunction().getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT));
		}
		return createAndUpdate(extraExpenseDto, ExtraExpenseDto.class, ExtraExpenseModel.class, extraExpenseDto.getId());
	}

}