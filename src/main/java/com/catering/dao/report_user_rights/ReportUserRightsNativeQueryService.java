package com.catering.dao.report_user_rights;

import java.util.List;

import com.catering.dto.tenant.GetReportUserRightsReportCategoryDto;

/**
 * Service interface for handling report user rights using native queries.
 * Provides methods to fetch report categories and rights for a specific user.
 * 
 * Key Responsibilities:
 * - Retrieve a list of report categories along with their report rights for a given user.
 * - Fetch report rights IDs for a specified user.
 * 
 * Implementing Class:
 * - {@link ReportUserRightsNativeQueryServiceImpl}
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
public interface ReportUserRightsNativeQueryService {

	/**
	 * Fetches a list of report categories along with their associated report rights for a given user.
	 * 
	 * @param userId the ID of the user whose report rights need to be fetched.
	 * @return a list of {@link GetReportUserRightsReportCategoryDto} containing the report categories and their rights.
	 */
	List<GetReportUserRightsReportCategoryDto> reportUserRightsReportCategoryDtoList(Long userId);

	/**
	 * Retrieves a list of report rights IDs for a specified user.
	 * 
	 * @param userId the ID of the user whose report rights IDs need to be fetched.
	 * @return a list of {@link Long} values representing the report rights IDs.
	 */
	List<Long> getRightsOfReportName(Long userId);

}