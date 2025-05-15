package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.ReportMasterDto;
import com.catering.service.tenant.ReportMasterService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(value = ApiPathConstant.REPORT_MASTER)
@Tag(name = SwaggerConstant.REPORT_MASTER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportMasterController {

	ReportMasterService reportMasterService;

	@GetMapping(value = ApiPathConstant.REPORT_CATEGORY_RANGE)
	public ResponseContainerDto<List<ReportMasterDto>> getReportsByCategoryRange() {
		return RequestResponseUtils.generateResponseDto(reportMasterService.getReportsByCategoryRange());
	}

}
