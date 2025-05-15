package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing the user rights for accessing specific reports in the database.
 * This class is mapped to the {@code report_user_rights} table.
 * 
 * <p>Fields included:</p>
 * <ul>
 * <li>{@code userId} - The ID of the user associated with the report rights (Foreign Key to user table).</li>
 * <li>{@code reportNameId} - The ID of the report for which rights are assigned (Foreign Key to report details table).</li>
 * <li>{@code canView} - A boolean indicating whether the user has view access to the report.</li>
 * </ul>
 * 
 * <p>This class extends {@code AuditByIdModel} to inherit auditing fields.</p>
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "report_user_rights")
public class ReportUserRightsModel extends AuditByIdModel {

	@Column(name = "fk_user_id")
	private Long userId;

	@Column(name = "fk_report_master_id")
	private Long reportNameId;

	@Column(name = "can_view")
	private Boolean canView;

}