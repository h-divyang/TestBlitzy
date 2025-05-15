package com.catering.dto.tenant;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing the user rights for accessing specific reports.
 * This class is used to transfer data related to the report rights assigned to a user.
 * 
 * <p>Fields included:</p>
 * <ul>
 * <li>{@code userId} - The ID of the user associated with the report rights.</li>
 * <li>{@code reportNameId} - The ID of the report for which rights are assigned.</li>
 * <li>{@code canView} - A boolean indicating whether the user has view access for the report.</li>
 * </ul>
 * 
 * <p>This class extends {@code AuditIdDto} to inherit audit-related properties.</p>
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
@Getter
@Setter
public class ReportUserRightsDto extends AuditIdDto {

	private Long userId;

	private Long reportNameId;

	private Boolean canView;

}