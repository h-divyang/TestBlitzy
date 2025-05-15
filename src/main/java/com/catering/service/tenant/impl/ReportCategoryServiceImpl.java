package com.catering.service.tenant.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.ReportCategoryDto;
import com.catering.model.tenant.ReportCategoryModel;
import com.catering.repository.tenant.ReportCategoryRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.ReportCategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of {@link ReportCategoryService} for managing report categories.
 * 
 * <p>This service interacts with the {@link ReportCategoryRepository} to retrieve data
 * from the database and uses {@link ModelMapperService} to map entities to DTOs.</p>
 * 
 * <p>Responsible for handling the business logic for fetching all report categories
 * and ensuring data is properly mapped and returned to the caller.</p>
 * 
 * @author Jayesh Soni
 * @since January 2025
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportCategoryServiceImpl implements ReportCategoryService {

	/**
	 * Repository for accessing report category data from the database.
	 */
	ReportCategoryRepository reportCategoryRepository;

	/**
	 * Service for mapping entities to DTOs and vice versa.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportCategoryDto> getAllReportCategory() {
		List<ReportCategoryModel> reportCategoryModels = reportCategoryRepository.findAll();
		return modelMapperService.convertListEntityAndListDto(reportCategoryModels, ReportCategoryDto.class);
	}

}