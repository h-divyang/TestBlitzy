package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.request.ReportCategoryDto;

/**
 * Service interface for managing report categories.
 * This interface provides method for retrieving report category data 
 * used in configuring report-related functionalities within the application.
 * 
 * <p>The service is responsible for fetching all available report categories.</p>
 * 
 * @see ReportCategoryDto
 * 
 * @author Jayesh Soni
 * @since January 2025
 */
public interface ReportCategoryService {

	/**
	 * Retrieves a list of all report categories.
	 *
	 * @return A list of {@link ReportCategoryDto} containing details of all report categories.
	 */
	public List<ReportCategoryDto> getAllReportCategory();

}