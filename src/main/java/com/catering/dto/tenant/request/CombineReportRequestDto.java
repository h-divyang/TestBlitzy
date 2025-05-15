package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CombineReportRequestDto extends OnlyIdDto {

	private ReportMasterDto reportMasterId;

}
