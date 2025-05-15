package com.catering.dao.report_user_rights;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.audit.OnlyIdDto;
import com.catering.dto.tenant.GetReportUserRightsReportCategoryDto;
import com.catering.dto.tenant.GetReportUserRightsReportNamesDto;

/**
 * Data Access Object (DAO) interface for executing native queries related to report user rights.
 * This interface leverages named native queries defined in the entity for fetching report categories,
 * report names with rights, and user-specific report rights.
 * 
 * <p>Methods provided:</p>
 * <ul>
 * <li>{@code reportUserRightsReportCategoryDto(Long userId)} - Fetches report categories for a user.</li>
 * <li>{@code reportUserRightsReportNamesDto(Long reportCategoryId, Long userId)} - Fetches report names with rights for a given category and user.</li>
 * <li>{@code getRightsOfReportName(Long userId)} - Retrieves the report rights IDs for a specific user.</li>
 * </ul>
 * 
 * <p>Queries used are defined as named native queries in the {@code ReportUserRightsNativeQuery} entity.</p>
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
public interface ReportUserRightsNativeQueryDao extends JpaRepository<ReportUserRightsNativeQuery, Long> {

	/**
	 * Fetches a list of report categories along with user rights for a specific user.
	 *
	 * @param userId The ID of the user.
	 * @return List of report categories with rights information.
	 */
	@Query(name = "getReportCategory", nativeQuery = true)
	List<GetReportUserRightsReportCategoryDto> reportUserRightsReportCategoryDto(Long userId);

	/**
	 * Fetches a list of report names along with rights for a specified report category and user.
	 *
	 * @param reportCategoryId The ID of the report category.
	 * @param userId The ID of the user.
	 * @return List of report names with rights information.
	 */
	@Query(name = "getReportNamesWithRights", nativeQuery = true)
	List<GetReportUserRightsReportNamesDto> reportUserRightsReportNamesDto(Long reportCategoryId, Long userId);

	/**
	 * Retrieves a list of report rights IDs for a specific user where the user has viewing rights.
	 *
	 * @param userId The ID of the user.
	 * @return List of IDs representing report rights for the user.
	 */
	@Query(name = "getRightsOfReportName", nativeQuery = true)
	List<OnlyIdDto> getRightsOfReportName(Long userId);

}