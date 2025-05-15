package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.ReportCategoryDto;
import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.model.tenant.ReportCompanyDetailRightsModel;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.ReportCategoryService;
import com.catering.service.tenant.ReportCompanyDetailRightsService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class that handles HTTP requests related to Report Company Detail Rights management.
 * This class defines provides endpoints for retrieving and updating report rights associated with report categories.
 * 
 * @author Jayesh Soni
 * @since January 2025
 */
@RestController
@RequestMapping(value = ApiPathConstant.REPORT_COMPANY_DETAIL_RIGHTS)
@Tag(name = SwaggerConstant.REPORT_COMPANY_DETAIL_RIGHTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportCompanyDetailRightsController {

	/**
	 * Service for handling report categories. 
	 */
	ReportCategoryService reportCategoryService;

	/** 
	 * Service for handling report company detail rights.
	 */
	ReportCompanyDetailRightsService reportCompanyDetailRightsService;

	/**
	 * Service for handling messaging operations.
	 */
	MessageService messageService;

	/**
	 * Retrieves all report categories.
	 *
	 * @return ResponseContainerDto containing a list of {@link ReportCategoryDto}.
	 */
	@GetMapping(value = ApiPathConstant.REPORT_CATEGORY)
	@AuthorizeUserRights(value = { ApiUserRightsConstants.REPORT_COMPANY_DETAIL_RIGHTS + ApiUserRightsConstants.CAN_VIEW })
	public ResponseContainerDto<List<ReportCategoryDto>> get() {
		return RequestResponseUtils.generateResponseDto(reportCategoryService.getAllReportCategory());
	}

	/**
	 * Retrieves report company detail rights by report category ID.
	 *
	 * @param categoryId The ID of the report category.
	 * @return ResponseContainerDto containing a list of {@link ReportCompanyDetailRightsDto}.
	 */
	@GetMapping
	public ResponseContainerDto<List<ReportCompanyDetailRightsDto>> getReportCompanyDetailRightsByReportCategoryId(@RequestParam Long categoryId) {
		return RequestResponseUtils.generateResponseDto(reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportCategoryId(categoryId));
	}

	/**
	 * Updates report company detail rights.
	 *
	 * @param reportCompanyDetailRightsModelList List of {@link ReportCompanyDetailRightsModel} to update.
	 * @return ResponseContainerDto containing a list of updated {@link ReportCompanyDetailRightsDto} along with a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = { ApiUserRightsConstants.REPORT_COMPANY_DETAIL_RIGHTS + ApiUserRightsConstants.CAN_EDIT })
	public ResponseContainerDto<List<ReportCompanyDetailRightsDto>> updateReportCompanyDetailRights(@RequestBody List<ReportCompanyDetailRightsModel> reportCompanyDetailRightsModelList) {
		return RequestResponseUtils.generateResponseDto(reportCompanyDetailRightsService.updateReportCompanyDetailRights(reportCompanyDetailRightsModelList), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}