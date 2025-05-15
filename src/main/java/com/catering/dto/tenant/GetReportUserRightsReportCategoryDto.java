package com.catering.dto.tenant;

import java.util.List;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) class that represents the user rights associated with a specific report category.
 * This class is used for transferring data between layers or components related to report user rights for 
 * different report categories in the system. It contains information such as the category name, a list of report names 
 * within the category, and the user ID associated with the report rights.
 * 
 * <p>It extends the {@link OnlyIdDto} class, inheriting the ID field for each report category object.</p>
 *
 * <p>The constructor initializes the fields of this DTO object, providing the ID, name of the category, 
 * and the user ID for setting up the user rights associated with the report category.</p>
 *
 * @since 2025-01-13
 * @author Krushali Talaviya
 */
@Getter
@Setter
public class GetReportUserRightsReportCategoryDto extends OnlyIdDto {

	private String name;

	private List<GetReportUserRightsReportNamesDto> reportUserRightsReportNamesDtoList;

	private Long userId;

	public GetReportUserRightsReportCategoryDto(Long id, String name, Long userId) {
		this.setId(id);
		this.name = name;
		this.userId = userId;
	}

}