package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.request.ReportMasterDto;

public interface ReportMasterService {

	/**
	 * Retrieves a list of reports filtered by a specific category range. The method
	 * fetches reports with category IDs between 1 and 7, excluding report ID 35,
	 * and applying additional filtering for category ID 7.
	 *
	 * @return a list of filtered reports as ReportMasterDto objects.
	 */
	List<ReportMasterDto> getReportsByCategoryRange();

}
