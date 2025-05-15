package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.model.tenant.ReportCompanyDetailRightsModel;
import com.catering.model.tenant.ReportMasterModel;
import com.catering.repository.tenant.ReportCompanyDetailRightsRepository;
import com.catering.repository.tenant.ReportMasterRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.ReportCompanyDetailRightsService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing report company detail rights.
 * This class provides methods to handle report company detail rights for different report categories and reports.
 * It extends the {@link ReportCompanyDetailRightsServiceImpl} class to inherit the service functionality.
 *
 * <p>The service is responsible for retrieving, updating, and managing report company detail rights settings for various reports.</p>
 *
 * @see ReportCompanyDetailRightsServiceImpl
 * @see ReportCompanyDetailRightsRepository
 * @see ModelMapperService
 * 
 * @author Jayesh Soni
 * @since January 2025
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportCompanyDetailRightsServiceImpl implements ReportCompanyDetailRightsService {

	/**
	 * Repository for CRUD operations on {@link ReportCompanyDetailRightsModel}.
	 */
	ReportCompanyDetailRightsRepository reportCompanyDetailRightsRepository;

	/**
	 * Repository for CRUD operations on {@link ReportMasterModel}.
	 */
	ReportMasterRepository reportMasterRepository;

	/**
	 * Service for mapping entities to DTOs and vice versa.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportCompanyDetailRightsDto> getReportCompanyDetailRightsByReportCategoryId(Long reportCategoryId) {
		List<ReportCompanyDetailRightsModel> reportCompanyDetailRightsModelList = new ArrayList<>();
		List<ReportMasterModel> reportMasterList = reportMasterRepository.findByReportCategoryId(reportCategoryId);
		reportMasterList.forEach(reportMaster -> {
			ReportCompanyDetailRightsModel reportCompanyDetailRightsModel = reportCompanyDetailRightsRepository.findByReportMasterId(reportMaster.getId());
			reportCompanyDetailRightsModelList.add(reportCompanyDetailRightsModel);
		});
		return modelMapperService.convertListEntityAndListDto(reportCompanyDetailRightsModelList, ReportCompanyDetailRightsDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportCompanyDetailRightsDto> updateReportCompanyDetailRights(List<ReportCompanyDetailRightsModel> reportComanyDetailRightsDtos) {
		reportComanyDetailRightsDtos.forEach(reportCompanyDetailRights -> DataUtils.setAuditFields(reportCompanyDetailRightsRepository, reportCompanyDetailRights.getId(), reportCompanyDetailRights));
		List<ReportCompanyDetailRightsModel> reportCompanyDetailRightsModelList = reportCompanyDetailRightsRepository.saveAll(reportComanyDetailRightsDtos);
		return modelMapperService.convertListEntityAndListDto(reportCompanyDetailRightsModelList, ReportCompanyDetailRightsDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportCompanyDetailRightsDto getReportCompanyDetailRightsByReportName(String reportName) {
		ReportMasterModel reportMasterModel = reportMasterRepository.findByReportName(reportName);
		ReportCompanyDetailRightsModel reportCompanyDetailRightsModel = reportCompanyDetailRightsRepository.findByReportMasterId(reportMasterModel.getId());
		return modelMapperService.convertEntityAndDto(reportCompanyDetailRightsModel, ReportCompanyDetailRightsDto.class);
	}

}