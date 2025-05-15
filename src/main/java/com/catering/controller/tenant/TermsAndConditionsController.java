package com.catering.controller.tenant;

import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.FileBean;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.TermsAndConditionsDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.TermsAndConditionsService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing terms and conditions.
 *
 * @RestController Indicates that this class is a Spring MVC controller handling RESTful requests.
 * @RequestMapping Maps the controller to a base URL path.
 * @Tag Describes the Swagger tag for this controller.
 *
 * @since 2023-12-18
 * @author Krushali Talaviya
 */
@RestController
@RequestMapping(value = ApiPathConstant.TERMS_AND_CONDITIONS)
@Tag(name = SwaggerConstant.TERMS_AND_CONDITIONS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TermsAndConditionsController {

	/**
	 * Service for managing terms and conditions.
	 */
	TermsAndConditionsService termsAndConditionsService;

	/**
	 * Service for handling messaging operations.
	 */
	MessageService messageService;

	/**
	 * POST method to create new terms and conditions data.
	 *
	 * @param termsAndConditionsDto The data to be created.
	 * @return ResponseContainerDto containing the result of the creation operation.
	 * @throws RestException If there is an error during the REST operation.
	 *
	 * @since 2023-12-18
	 * @author Krushali Talaviya
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TERMS_AND_CONDITIONS + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.TERMS_AND_CONDITIONS + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<Optional<TermsAndConditionsDto>> create(@Valid @RequestBody TermsAndConditionsDto termsAndConditionsDto) throws RestException {
		Optional<TermsAndConditionsDto> termsAndConditionsData = termsAndConditionsService.saveTermsAndConitionsData(termsAndConditionsDto);
		return RequestResponseUtils.generateResponseDto(termsAndConditionsData, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * GET method to retrieve terms and conditions data.
	 *
	 * @return ResponseContainerDto containing the retrieved terms and conditions data.
	 *
	 * @since 2023-12-18
	 * @author Krushali Talaviya
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TERMS_AND_CONDITIONS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<TermsAndConditionsDto> read() {
		return RequestResponseUtils.generateResponseDto(termsAndConditionsService.getTermsAndConditionsData());
	}

	/**
	 * Endpoint to generate and retrieve the Terms and Conditions report in PDF format.
	 *
	 * @param langType The language type for the report. Optional parameter, defaults to system default if not provided.
	 * @return A {@code ResponseContainerDto} containing a byte array representing the generated PDF file.
	 *         If the file is not generated, the response will contain a null file.
	 *
	 */
	@GetMapping(value = ApiPathConstant.TERMS_AND_CONTIONS_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TERMS_AND_CONDITIONS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> printToPdf(@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType) {
		FileBean file = FileBean.builder().file(termsAndConditionsService.getTermsAndConditionsInByte(langType)).contentType(MediaType.APPLICATION_PDF.toString()).build();
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}