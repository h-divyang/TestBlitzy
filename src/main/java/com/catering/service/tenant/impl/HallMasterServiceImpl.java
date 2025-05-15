package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.FieldConstants;
import com.catering.constant.FileConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.HallMasterDto;
import com.catering.model.tenant.HallMasterModel;
import com.catering.repository.tenant.HallMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.S3Service;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.HallMasterService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link HallMasterService} interface for managing HallMaster-related operations.
 * This class extends {@link GenericServiceImpl} and provides specific implementations and additional features.
 *
 * It handles operations such as saving HallMaster records with optional image upload, fetching data with filters,
 * updating the status of records, retrieving all active HallMaster records, and deletion with optional image cleanup.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HallMasterServiceImpl extends GenericServiceImpl<HallMasterDto, HallMasterModel, Long> implements HallMasterService {

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Hall Master entities.
	 */
	HallMasterRepository hallMasterRepository;

	/**
	 * Service for handling file operations like upload and retrieval.
	 */
	FileService fileService;

	/**
	 * Service for interacting with Amazon S3 storage.
	 */
	S3Service s3Service;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<HallMasterDto> save(HallMasterDto hallMasterDto, MultipartFile image) {
		ValidationUtils.validateFields(hallMasterRepository, exceptionService, messageService, hallMasterDto.getNameDefaultLang(), hallMasterDto.getNamePreferLang(), hallMasterDto.getNameSupportiveLang(), hallMasterDto.getId());
		hallMasterDto = createAndUpdate(hallMasterDto, HallMasterDto.class, HallMasterModel.class, hallMasterDto.getId());
		if (Objects.nonNull(image)) {
			fileService.upload(image, fileService.createKey(FileConstants.MODULE_HALL_MASTER, hallMasterDto.getId().toString()));
		}
		return Optional.of(hallMasterDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<HallMasterDto>> read(FilterDto filterDto) {
		Optional<Example<HallMasterModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			HallMasterModel hallMasterModel = HallMasterModel.ofSearchingModel(query);
			hallMasterModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(hallMasterModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<HallMasterDto>> hallMasterDtoResponseDto = read(HallMasterDto.class, HallMasterModel.class, filterDto, example);
		hallMasterDtoResponseDto.getBody().forEach(hallMasterDto -> {
			hallMasterDto.setImage(fileService.getUrl(FileConstants.MODULE_HALL_MASTER, hallMasterDto.getId().toString())); 
		});
		return hallMasterDtoResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id, Boolean isImage) {
		if (!Boolean.TRUE.equals(isImage)) { // It will check if isImage is null or isImage is false both in one
			try {
				deleteById(id);
				s3Service.delete(fileService.createKey(FileConstants.MODULE_HALL_MASTER, id.toString()));
			} catch (DataIntegrityViolationException e) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
			}
		}
		s3Service.delete(fileService.createKey(FileConstants.MODULE_HALL_MASTER, id.toString()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<HallMasterDto> updateStatus(Long id, Boolean status) {
		Optional<HallMasterModel> hallMasterModel = hallMasterRepository.findById(id);
		hallMasterModel.get().setIsActive(status);
		return Optional.of(modelMapperService.convertEntityAndDto(hallMasterRepository.save(hallMasterModel.get()), HallMasterDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HallMasterDto> getAllHallMaster() {
		return modelMapperService.convertListEntityAndListDto(hallMasterRepository.findByIsActiveTrue(), HallMasterDto.class);
	}

	/**
	 * Retrieves an ExampleMatcher configured with specific matchers and ignore paths.
	 *
	 * @return An ExampleMatcher configured with matchers for common field names in default, preferred, and supportive languages, and with ignore paths for audit fields.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName()));
	}

}