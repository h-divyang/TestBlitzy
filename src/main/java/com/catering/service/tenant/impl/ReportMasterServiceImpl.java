package com.catering.service.tenant.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.ReportMasterDto;
import com.catering.model.tenant.ReportMasterModel;
import com.catering.repository.tenant.ReportMasterRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.ReportMasterService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportMasterServiceImpl implements ReportMasterService {

	ModelMapperService modelMapperService;

	ReportMasterRepository reportMasterRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportMasterDto> getReportsByCategoryRange() {
		List<ReportMasterModel> reportByCategoryRange = reportMasterRepository.findByReportCategoryIdBetween(1L, 7L);
		List<ReportMasterModel> filteredReports = reportByCategoryRange.stream().filter(report -> report.getId() != 35)
				.filter(report -> !(report.getReportCategory().getId() == 7 && report.getId() != 39)).toList();
		return modelMapperService.convertListEntityAndListDto(filteredReports, ReportMasterDto.class);
	}

}
