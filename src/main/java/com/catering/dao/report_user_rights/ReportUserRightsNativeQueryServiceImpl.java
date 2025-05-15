package com.catering.dao.report_user_rights;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.audit.OnlyIdDto;
import com.catering.dto.tenant.GetReportUserRightsReportCategoryDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for handling report user rights using native queries.
 * Provides methods to retrieve report categories and report rights for a specific user.
 * 
 * Key Responsibilities:
 * - Fetches a list of report categories with associated report rights for a given user.
 * - Retrieves report rights IDs for a specified user.
 * 
 * Dependencies:
 * - {@link ReportUserRightsNativeQueryDao} for executing native queries.
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportUserRightsNativeQueryServiceImpl implements ReportUserRightsNativeQueryService {

	/**
	 * Data Access Object for performing native queries related to report user rights.
	 * This DAO is responsible for executing complex SQL queries to fetch report rights data 
	 * for specific users directly from the database.
	 */
	ReportUserRightsNativeQueryDao reportUserRightsNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetReportUserRightsReportCategoryDto> reportUserRightsReportCategoryDtoList(Long userId) {
		List<GetReportUserRightsReportCategoryDto> reportUserRightsReportCategoryDtos = reportUserRightsNativeQueryDao.reportUserRightsReportCategoryDto(userId);
		reportUserRightsReportCategoryDtos.forEach(reportRight -> reportRight.setReportUserRightsReportNamesDtoList(reportUserRightsNativeQueryDao.reportUserRightsReportNamesDto(reportRight.getId(), userId)));
		return reportUserRightsReportCategoryDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getRightsOfReportName(Long userId) {
		return reportUserRightsNativeQueryDao.getRightsOfReportName(userId).stream().map(OnlyIdDto::getId).toList();
	}

}