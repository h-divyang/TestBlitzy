package com.catering.controller.tenant;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CompanyBankDto;
import com.catering.properties.FileProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanyBankService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing company bank-related operations such as retrieving,
 * updating, and deleting bank details and associated QR codes.
 */
@RestController
@RequestMapping(value = ApiPathConstant.COMPANY_BANK)
@Tag(name = SwaggerConstant.COMPANY_BANK)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyBankController {

	Logger logger = LoggerFactory.getLogger(CompanyBankController.class);

	/**
	 * Service for handling operations related to company bank details.
	 */
	CompanyBankService companyBankService;

	/**
	 * Service for handling messages or notifications within the application.
	 */
	MessageService messageService;

	/**
	 * Configuration properties for file handling within the application.
	 */
	FileProperties fileProperties;

	/**
	 * Service for managing exceptions and error handling.
	 */
	ExceptionService exceptionService;

	/**
	 * Utility for mapping Java objects to JSON data and vice versa.
	 */
	ObjectMapper objectMapper;

	/**
	 * Retrieves the details of a company bank account.
	 *
	 * @return a ResponseContainerDto containing an Optional of CompanyBankDto with the company bank details,
	 *		   or an empty Optional if no details are available.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.COMPANY_PROFILE + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Optional<CompanyBankDto>> read() {
		return RequestResponseUtils.generateResponseDto(companyBankService.read());
	}

	/**
	 * Updates the company bank details with the provided information and optionally attaches a QR code image.
	 *
	 * @param bankDetails A JSON string representation of the bank details to be updated, must not be null or empty.
	 * @param qrCode An optional MultipartFile containing the QR code image, can be null.
	 * @return A ResponseContainerDto containing the updated CompanyBankDto object along with a success message.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.COMPANY_PROFILE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<CompanyBankDto> update(@Valid @RequestParam(required = true) String bankDetails, @RequestParam(name= Constants.IMAGE, required = false) MultipartFile qrCode) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(qrCode, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		CompanyBankDto dto = null;
		try {
			dto = objectMapper.readValue(bankDetails, CompanyBankDto.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		Optional<CompanyBankDto> companyBankResponseDto = companyBankService.createAndUpdate(dto, qrCode);
		return RequestResponseUtils.generateResponseDto(companyBankResponseDto.isPresent() ? companyBankResponseDto.get() : null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes the QR code associated with the company bank account.
	 *
	 * @return a ResponseContainerDto containing a null object and a success message indicating that the data has been deleted.
	 */
	@DeleteMapping(value = ApiPathConstant.QR_CODE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.COMPANY_PROFILE + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete() {
		companyBankService.deleteQrCode();
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}