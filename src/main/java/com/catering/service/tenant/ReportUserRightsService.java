package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.ReportUserRightsDto;
import com.catering.dto.tenant.ReportUserRightsWithUsersDto;
import com.catering.model.tenant.ReportUserRightsModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing report user rights.
 * This interface defines the core functionalities related to managing user rights 
 * for accessing various reports in the system.
 * 
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 * <li>Retrieve report user rights for all users along with their associated report categories.</li>
 * <li>Save or update report user rights based on provided DTOs.</li>
 * </ul>
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
public interface ReportUserRightsService extends GenericService<ReportUserRightsDto, ReportUserRightsModel, Long> {

	/**
	 * Retrieves a list of all report user rights with user and report category details.
	 * @return A list of ReportUserRightsWithUsersDto containing report rights information.
	 */
	List<ReportUserRightsWithUsersDto> getReportUserRights();

	/**
	 * Saves or updates the provided report user rights data.
	 * If a report right already exists, it updates the entry; otherwise, a new entry is created.
	 * @param reportUserRightsDtos A list of ReportUserRightsDto to be saved or updated.
	 * @param uniqueCode A unique identifier used for cache management.
	 * @return A list of updated ReportUserRightsDto.
	 */
	List<ReportUserRightsDto> saveOrUpdateReportUserRight(List<ReportUserRightsDto> reportUserRightsDtos, String uniqueCode);

}