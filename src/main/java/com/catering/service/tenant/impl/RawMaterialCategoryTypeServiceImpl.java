package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.RawMaterialCategoryTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialCategoryTypeModel;
import com.catering.repository.tenant.RawMaterialCategoryTypeRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.RawMaterialCategoryTypeService;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link RawMaterialCategoryTypeService} interface for managing raw material category types.
 * Provides functionality to perform CRUD operations, data validation, and custom logic specific to raw material category types.
 * Extends {@link GenericServiceImpl} to inherit common service functionalities.
 * This service integrates with multiple system components including a repository, exception handling, messaging,
 * model mapping, and company preferences management.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialCategoryTypeServiceImpl implements RawMaterialCategoryTypeService {

	/**
	 * Repository for managing raw material category types.
	 */
	RawMaterialCategoryTypeRepository rawMaterialCategoryTypeRepository;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Constant for the raw material category type.
	 */
	String RAW_MATERIAL_CATEGORY_TYPE = "Raw Material Category Type";

	/**
	 * Constant for the raw material category type ID, derived from the category type.
	 */
	String RAW_MATERIAL_CATEGORY_TYPE_ID = RAW_MATERIAL_CATEGORY_TYPE + " ID";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<RawMaterialCategoryTypeDto>> read(FilterDto filterDto) {
		List<RawMaterialCategoryTypeModel> rawMaterialCategoryTypeModel = rawMaterialCategoryTypeRepository.findAll();
		List<RawMaterialCategoryTypeDto> rawMaterialCategoryTypeDto = rawMaterialCategoryTypeModel.stream().map(rawMaterialCategoryType -> modelMapperService.convertEntityAndDto(rawMaterialCategoryType, RawMaterialCategoryTypeDto.class)).collect(Collectors.toList());
		return RequestResponseUtils.generateResponseDto(rawMaterialCategoryTypeDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !rawMaterialCategoryTypeRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, RAW_MATERIAL_CATEGORY_TYPE_ID));
		}
	}

}