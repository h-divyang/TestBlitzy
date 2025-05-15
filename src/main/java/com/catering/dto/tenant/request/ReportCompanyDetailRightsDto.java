package com.catering.dto.tenant.request;

import com.catering.model.audit.AuditByIdModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCompanyDetailRightsDto extends AuditByIdModel {

	private ReportMasterDto reportMaster;

	private Boolean companyLogo;

	private Boolean companyName;

	private Boolean companyAddress;

	private Boolean companyEmail;

	private Boolean companyMobileNumber;

	private Boolean orderBookingNotes;

	private Boolean termsAndConditions;

	private Boolean backgroundImage;

}