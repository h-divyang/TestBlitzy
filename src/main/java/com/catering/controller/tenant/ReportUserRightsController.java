package com.catering.controller.tenant;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.report_user_rights.ReportUserRightsNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.ReportUserRightsDto;
import com.catering.dto.tenant.ReportUserRightsWithUsersDto;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.ReportUserRightsService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for handling API requests related to user rights for reports.
 * This class exposes endpoints for reading, saving, and managing report user rights.
 * 
 * <p>Endpoints:</p>
 * <ul>
 * <li>{@code GET /report-user-rights} - Retrieves a list of report user rights with user details.</li>
 * <li>{@code PUT /report-user-rights} - Updates or saves the user rights for reports.</li>
 * <li>{@code GET /report-user-rights/{id}} - Retrieves a list of report rights associated with a specific user.</li>
 * </ul>
 * 
 * <p>This class uses the {@code ReportUserRightsService} to manage report rights and the {@code ReportUserRightsNativeQueryService}
 * for custom queries related to report rights.</p>
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
@RestController
@RequestMapping(value = ApiPathConstant.REPORT_USER_RIGHTS)
@Tag(name = SwaggerConstant.REPORT_USER_RIGHTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportUserRightsController {

	/**
	 * Service to handle business logic related to report user rights.
	 */
	ReportUserRightsService reportUserRightsService;

	/**
	 * Service to manage messages used in the responses.
	 */
	MessageService messageService;

	/**
	 * Service for custom native queries related to report user rights.
	 */
	ReportUserRightsNativeQueryService reportUserRightsNativeQueryService;

	/**
	 * Endpoint to retrieve all report user rights associated with users.
	 * Only users with appropriate view rights can access this endpoint.
	 * 
	 * @return A {@link ResponseContainerDto} containing a list of {@link ReportUserRightsWithUsersDto}.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER_RIGHTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<ReportUserRightsWithUsersDto>> read() {
		return RequestResponseUtils.generateResponseDto(reportUserRightsService.getReportUserRights());
	}

	/**
	 * Endpoint to save or update report user rights. Only users with appropriate edit rights can access this endpoint.
	 * 
	 * @param reportUserRightsDto A list of {@link ReportUserRightsDto} containing the data to be saved or updated.
	 * @param uniqueCode A unique code for caching purposes.
	 * @return A {@link ResponseContainerDto} with a message indicating the result.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER_RIGHTS + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<List<ReportUserRightsDto>> save(@RequestBody List<ReportUserRightsDto> reportUserRightsDto, @RequestParam String uniqueCode) {
		return RequestResponseUtils.generateResponseDto(reportUserRightsService.saveOrUpdateReportUserRight(reportUserRightsDto, uniqueCode), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to retrieve the list of report rights for a specific user.
	 * This endpoint is cached using the unique code for efficient repeated access.
	 * 
	 * @param id The ID of the user whose report rights are being queried.
	 * @param uniqueCode A unique code for caching purposes.
	 * @return A {@link ResponseContainerDto} containing a list of Long IDs representing the report rights.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@Cacheable(value = Constants.REPORT_RIGHTS_CACHE, key="#id+'-'+#uniqueCode")
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER_RIGHTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<Long>> getReportUserRightsList(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam String uniqueCode) {
		return RequestResponseUtils.generateResponseDto(reportUserRightsNativeQueryService.getRightsOfReportName(id));
	}

}