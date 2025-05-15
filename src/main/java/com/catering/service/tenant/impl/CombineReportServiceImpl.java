package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CombineReportDto;
import com.catering.model.tenant.CombineReportModel;
import com.catering.repository.tenant.CombineReportRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.CombineReportService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CombineReportServiceImpl implements CombineReportService {

	/**
	 * Repository for managing combined reports.
	 */
	CombineReportRepository combineReportRepository;

	/**
	 * Service for mapping between entity and DTO objects.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAndUpdate(List<CombineReportDto> combineReportRequest) {
		List<CombineReportModel> combinedReportModels = new ArrayList<>();

		// Extract IDs from incoming payload
		List<Long> incomingIds = combineReportRequest.stream().map(CombineReportDto::getId).filter(Objects::nonNull).toList();

		// Fetch existing records from DB
		List<CombineReportModel> existingReports = combineReportRepository.findAll();

		// Find records to delete (IDs that exist in DB but are not in the incoming list)
		List<CombineReportModel> reportsToDelete = existingReports.stream().filter(report -> !incomingIds.contains(report.getId())).toList();

		// Delete missing parent records from combined_report table
		combineReportRepository.deleteAll(reportsToDelete);
		combineReportRequest.forEach(combinedReport -> {
			CombineReportModel combinedReportModel = modelMapperService.convertEntityAndDto(combinedReport, CombineReportModel.class);
			DataUtils.setAuditFields(combineReportRepository, combinedReportModel.getId(), combinedReportModel);
			combinedReportModels.add(combinedReportModel);
		});
		combineReportRepository.saveAll(combinedReportModels);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CombineReportDto> read() {
		return modelMapperService.convertListEntityAndListDto(combineReportRepository.findAll(), CombineReportDto.class);
	}

}