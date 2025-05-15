package com.catering.dto.tenant;

import java.util.List;

import com.catering.dto.tenant.request.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO class representing the report user rights along with associated users.
 * This class extends {@link UserDto} and includes a list of report categories with their respective rights.
 * 
 * The {@code reportUserRightsReportCategoryDtoList} property holds a collection of 
 * {@link GetReportUserRightsReportCategoryDto} objects, each representing a category with the 
 * report rights available for the user.
 * 
 * Key Features:
 * <ul>
 *   <li>Inherits user-related data from the {@code UserDto} class.</li>
 *   <li>Provides a list of report categories and their rights.</li>
 * </ul>
 * 
 * @since 2025-01-13
 * @author Krushali Talaviya
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportUserRightsWithUsersDto extends UserDto {

	List<GetReportUserRightsReportCategoryDto> reportUserRightsReportCategoryDtoList;

}