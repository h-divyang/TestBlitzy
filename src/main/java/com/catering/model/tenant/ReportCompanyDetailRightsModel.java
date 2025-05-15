package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report_company_detail_rights")
public class ReportCompanyDetailRightsModel extends AuditByIdModel {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_report_master_id")
	private ReportMasterModel reportMaster;

	@Column(name = "company_logo")
	private Boolean companyLogo;

	@Column(name = "company_name")
	private Boolean companyName;

	@Column(name = "company_address")
	private Boolean companyAddress;

	@Column(name = "company_email")
	private Boolean companyEmail;

	@Column(name = "company_mobile_number")
	private Boolean companyMobileNumber;

	@Column(name = "order_booking_notes")
	private Boolean orderBookingNotes;

	@Column(name = "terms_and_conditions")
	private Boolean termsAndConditions;

	@Column(name = "background_image")
	private Boolean backgroundImage;

}