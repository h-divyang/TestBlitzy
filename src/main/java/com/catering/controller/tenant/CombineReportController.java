package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CombineReportDto;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CombineReportService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(value = ApiPathConstant.COMBINE_REPORT)
@Tag(name = SwaggerConstant.COMBINE_REPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CombineReportController {

	/**
	 * Service providing method of save and retrieve related to combine report configuration.
	 */
	CombineReportService combineReportService;

	/**
	 * Service used to retrieve messages for localization.
	 */
	MessageService messageService;

	/**
	 * Creates or updates combined report entries.
	 *
	 * @param combineReportRequest List of {@link CombineReportDto} objects containing report data to be created or updated.
	 * @return A {@link ResponseContainerDto} containing a success message after processing the request.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> createAndUpdate(@RequestBody List<CombineReportDto> combineReportRequest) {
		combineReportService.createAndUpdate(combineReportRequest);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves all combined report entries.
	 *
	 * @return A {@link ResponseContainerDto} containing the list of combined reports.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Object> read() {
		return RequestResponseUtils.generateResponseDto(combineReportService.read());
	}

}
