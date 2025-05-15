package com.catering.service.tenant.impl;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.common.RecordExistDto;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.CompanyPreferencesRegistrationDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.model.tenant.CompanyPreferencesModel;
import com.catering.model.tenant.CompanyUserModelForAudit;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CompanyPreferencesService} interface for handling operations
 * related to company preferences. This class provides methods for retrieving, updating,
 * saving, and checking the active status of company preferences, while utilizing various
 * supporting services and integrating with the repository layer.
 *
 * This class extends {@link GenericServiceImpl} to leverage common CRUD functionality
 * and adds specific logic for managing company preferences.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyPreferencesServiceImpl extends GenericServiceImpl<CompanyPreferencesDto, CompanyPreferencesModel, Long> implements CompanyPreferencesService {

	Logger logger = LoggerFactory.getLogger(CompanyPreferencesServiceImpl.class);

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for accessing and managing company preferences data.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/**
	 * Service for handling custom exceptions and exception logic.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for managing and delivering localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling file-related operations such as upload, download, and management.
	 */
	FileService fileService;

	/**
	 * Manager for handling caching functionality within the application.
	 */
	CacheManager cacheManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyPreferencesDto> find() {
		Optional<CompanyPreferencesModel> companyPreferencesModelOptional = companyPreferencesRepository.findAll().stream().findFirst();
		if (companyPreferencesModelOptional.isPresent()) {
			CompanyPreferencesDto companyPreferencesDto = modelMapperService.convertEntityAndDto(companyPreferencesModelOptional.get(), CompanyPreferencesDto.class);
			companyPreferencesDto.setLogo(fileService.getUrl(Constants.LOGO));
			return Optional.of(companyPreferencesDto);
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyPreferencesDto> update(CompanyPreferencesDto companyPreferencesDto, MultipartFile logo, HttpServletRequest request) {
		boolean isExistsMobile = Objects.nonNull(companyPreferencesDto.getId()) ? companyPreferencesRepository.existsByMobileNumberAndIdNot(companyPreferencesDto.getMobileNumber(), companyPreferencesDto.getId()) : companyPreferencesRepository.existsByMobileNumber(companyPreferencesDto.getMobileNumber());
		if (isExistsMobile) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.ALREADY_EXIST), RecordExistDto.builder().isExistMobile(Boolean.TRUE).build());
		}
		if (!companyPreferencesRepository.existsById(companyPreferencesDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (Objects.nonNull(logo)) {
			String path = fileService.createKey(Constants.LOGO);
			fileService.upload(logo, path);
			// Update the logo in the cache memory
			Object tenant = request.getAttribute(Constants.TENANT);
			Cache cache = cacheManager.getCache(Constants.COMPANY_LOGO_CACHE);
			if(Objects.nonNull(cache)) {
				cache.put(tenant.toString() + "-" + Constants.LOGO, fileService.getUrl(Constants.LOGO));
			}
		}
		CompanyPreferencesModel companyPreferenceModel = modelMapperService.convertEntityAndDto(companyPreferencesDto, CompanyPreferencesModel.class);
		DataUtils.setAuditFields(companyPreferencesRepository, companyPreferencesDto.getId(), companyPreferenceModel);
		return Optional.of(modelMapperService.convertEntityAndDto(companyPreferencesRepository.save(companyPreferenceModel), CompanyPreferencesDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(CompanyPreferencesRegistrationDto companyPreferencesDto, CompanyUserRegistrationDto companyUserDto) {
		CompanyPreferencesModel companyPreferencesModel = modelMapperService.convertEntityAndDto(companyPreferencesDto, CompanyPreferencesModel.class);
		DataUtils.setAuditFields(companyPreferencesRepository, companyPreferencesDto.getId(), companyPreferencesModel);
		CompanyUserModelForAudit companyUserModel = modelMapperService.convertEntityAndDto(companyUserDto, CompanyUserModelForAudit.class);
		companyPreferencesModel.setCreatedBy(companyUserModel);
		companyPreferencesModel.setUpdatedBy(companyUserModel);
		modelMapperService.convertEntityAndDto(companyPreferencesRepository.save(companyPreferencesModel), CompanyPreferencesDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isActive() {
		return companyPreferencesRepository.isCompanyActive();
	}

}