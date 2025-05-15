package com.catering.service.superadmin.impl;

import org.springframework.stereotype.Service;
import com.catering.constant.Constants;
import com.catering.dto.superadmin.CateringPreferencesDto;
import com.catering.model.superadmin.CateringPreferencesModel;
import com.catering.repository.superadmin.CateringPreferencesRepository;
import com.catering.service.common.FileService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.superadmin.CateringPreferencesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CateringPreferencesServiceImpl implements CateringPreferencesService {

	/**
	 * Repository for managing catering preferences data.
	 */
	CateringPreferencesRepository cateringPreferencesRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for handling file-related operations.
	 */
	FileService fileService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CateringPreferencesDto read() {
		CateringPreferencesModel cateringPreferencesModel = cateringPreferencesRepository.findFirstByOrderByIdAsc();
		return modelMapperService.convertEntityAndDto(cateringPreferencesModel, CateringPreferencesDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogoHorizontal() {
		return fileService.createUrl(Constants.LOGO_HORIZONTAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogoHorizontalWhite() {
		return fileService.createUrl(Constants.LOGO_HORIZONTAL_WHITE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogoVertical() {
		return fileService.createUrl(Constants.LOGO_VERTICAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFavicon() {
		return fileService.createUrl(Constants.FAVICON);
	}

}