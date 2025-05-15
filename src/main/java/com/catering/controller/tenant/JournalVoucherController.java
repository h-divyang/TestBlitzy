package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.JournalVoucherDto;
import com.catering.dto.tenant.request.JournalVoucherResponseDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.JournalVoucherService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing Journal Vouchers.
 * <p>
 * This controller provides endpoints for creating, updating, retrieving, and deleting Journal Vouchers.
 * Each method is secured with role-based authorization to ensure that only users with the appropriate
 * permissions can perform these operations.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.JOURNAL_VOUCHER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = SwaggerConstant.JOURNAL_VOUCHER)
public class JournalVoucherController {

	/**
	 * Service to manage messages and retrieve localized response messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling exceptions consistently across the application.
	 */
	ExceptionService exceptionService;

	/**
	 * Service layer containing business logic for Journal Voucher operations.
	 */
	JournalVoucherService journalVoucherService;

	/**
	 * Creates or updates a Journal Voucher.
	 *
	 * @param journalVoucher the DTO containing the Journal Voucher details to create or update.
	 * @return a response container DTO containing the created or updated Journal Voucher.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<JournalVoucherDto>> createAndUpdate(@Valid @RequestBody JournalVoucherDto journalVoucher) {
		Optional<JournalVoucherDto> journalVoucherDto = journalVoucherService.createAndUpdate(journalVoucher);
		return RequestResponseUtils.generateResponseDto(journalVoucherDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing Journal Voucher.
	 *
	 * @param journalVoucher the DTO containing the updated Journal Voucher details.
	 * @return a response container DTO containing the updated Journal Voucher.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<JournalVoucherDto>> update(@Valid @RequestBody JournalVoucherDto journalVoucher) {
		Optional<JournalVoucherDto> journalVoucherDto = journalVoucherService.createAndUpdate(journalVoucher);
		return RequestResponseUtils.generateResponseDto(journalVoucherDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves all Journal Vouchers based on the provided filter criteria.
	 *
	 * @param filterDto the filter criteria to apply when retrieving Journal Vouchers.
	 * @return a response container DTO containing a list of Journal Voucher response objects.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<JournalVoucherResponseDto>> getAllJournalVoucher(FilterDto filterDto) {
		return journalVoucherService.getAllJournalVoucher(filterDto);
	}

	/**
	 * Retrieves a specific Journal Voucher by its ID.
	 *
	 * @param id the ID of the Journal Voucher to retrieve, validated as a numeric value.
	 * @return a response container DTO containing the requested Journal Voucher details.
	 * @throws RestException if the ID is invalid or the Journal Voucher is not found.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<JournalVoucherDto>> getJournalVoucher(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) throws RestException {
		return RequestResponseUtils.generateResponseDto(journalVoucherService.getJournalVoucher(Long.valueOf(id)));
	}

	/**
	 * Deletes a Journal Voucher by its ID.
	 *
	 * @param id the ID of the Journal Voucher to delete, validated as a numeric value.
	 * @return a response container DTO confirming the deletion.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> deleteJournalVoucher(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		if(!ValidationUtils.isNumber(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		journalVoucherService.deleteJournalVoucher(Long.valueOf(id));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}