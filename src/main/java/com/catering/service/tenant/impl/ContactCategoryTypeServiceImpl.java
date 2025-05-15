package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCategoryTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactCategoryTypeModel;
import com.catering.repository.tenant.ContactCategoryTypeRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.ContactCategoryTypeService;
import com.catering.util.RequestResponseUtils;

/**
 * Implementation of the ContactCategoryTypeService interface, extending the generic service implementation
 * for ContactCategoryType entities and DTOs. This class provides functionality for managing
 * ContactCategoryType entities, including creation, updating, validation, and retrieval operations.
 */
@Service
public class ContactCategoryTypeServiceImpl implements ContactCategoryTypeService {

	/**
	 * Service for mapping between DTOs and entities.
	 */
	@Autowired
	private ModelMapperService modelMapperService;

	/**
	 * Repository for managing ContactCategoryType entities.
	 */
	@Autowired
	private ContactCategoryTypeRepository contactCategoryTypeRepository;

	/**
	 * Service for handling and throwing application-specific exceptions.
	 */
	@Autowired
	private ExceptionService exceptionService;

	/**
	 * Service for managing and retrieving localized messages.
	 */
	@Autowired
	private MessageService messageService;

	/**
	 * Constant for the entity name 'Contact Category Type'.
	 */
	private static final String CONTACT_CATEGORY_TYPE = "Contact Category Type";

	/**
	 * Constant for the identifier field name of 'Contact Category Type'.
	 */
	private static final String CONTACT_CATEGORY_TYPE_ID = CONTACT_CATEGORY_TYPE + " ID";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !contactCategoryTypeRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, CONTACT_CATEGORY_TYPE_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<ContactCategoryTypeDto>> read(FilterDto filterDto) {
		List<ContactCategoryTypeModel> contactCategoryTypes = contactCategoryTypeRepository.findAll();
		List<ContactCategoryTypeDto> contactCategoryTypesDtos = contactCategoryTypes.stream().map(contactCategoryType -> modelMapperService.convertEntityAndDto(contactCategoryType, ContactCategoryTypeDto.class)).collect(Collectors.toList());
		return RequestResponseUtils.generateResponseDto(contactCategoryTypesDtos);
	}

}