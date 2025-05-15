package com.catering.dto.tenant;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) class that represents the user rights for a specific report name.
 * This class is used to transfer data between layers or components related to user rights for individual reports
 * within a report category. It contains the report name, associated report rights ID, and the view access permission for the user.
 * 
 * <p>This class extends the {@link OnlyIdDto} class, inheriting the ID field.</p>
 * 
 * <p>The class uses Lombok annotations to generate getter, setter, builder, and other utility methods, making the object 
 * creation and manipulation more efficient.</p>
 *
 * @since 2025-01-13
 * @author Krushali Talaviya
 */
@Getter
@Setter
public class GetReportUserRightsReportNamesDto extends OnlyIdDto {

	private String name;

	private Long reportRightsId;

	private Boolean canView;

	public GetReportUserRightsReportNamesDto(Long id, String name, Long reportRightsId, Boolean canView) {
		this.setId(id);
		this.name = name;
		this.reportRightsId = reportRightsId;
		this.canView = canView;
	}

}