package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.model.tenant.ReportCompanyDetailRightsModel;

/**
 * Service interface for managing Report Company Detail Rights.
 * 
 * <p>
 * This service provides methods to perform operations related to report company
 * detail rights, such as retrieving, updating, and fetching details based on
 * report names or categories.
 * </p>
 * 
 * <p>
 * The methods in this interface are designed to interact with the database and
 * ensure proper mapping of entities to DTOs for client responses.
 * </p>
 * 
 * @see ReportCompanyDetailRightsModel
 * @see ReportCompanyDetailRightsDto
 * 
 * @author Jayesh Soni
 * @since January 2025
 */
public interface ReportCompanyDetailRightsService {

	/**
	 * Retrieves a list of {@link ReportCompanyDetailRightsDto} by the given report category ID.
	 * 
	 * @param reportCategoryId The ID of the report category.
	 * @return A list of {@link ReportCompanyDetailRightsDto} for the specified category.
	 */
	List<ReportCompanyDetailRightsDto> getReportCompanyDetailRightsByReportCategoryId(Long reportCategoryId);

	/**
	 * Updates the list of Report Company Detail Rights.
	 * 
	 * @param reportComanyDetailRightsDtos The list of {@link ReportCompanyDetailRightsModel} to update.
	 * @return A list of updated {@link ReportCompanyDetailRightsDto}.
	 */
	List<ReportCompanyDetailRightsDto> updateReportCompanyDetailRights(
			List<ReportCompanyDetailRightsModel> reportComanyDetailRightsDtos);

	/**
	 * Retrieves a {@link ReportCompanyDetailRightsDto} by the given report name.
	 * 
	 * @param reportName The name of the report.
	 * @return The {@link ReportCompanyDetailRightsDto} corresponding to the given report name.
	 */
	ReportCompanyDetailRightsDto getReportCompanyDetailRightsByReportName(String reportName);

}