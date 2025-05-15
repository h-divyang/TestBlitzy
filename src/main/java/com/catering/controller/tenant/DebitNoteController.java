package com.catering.controller.tenant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

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
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.DebitNoteGetByIdDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRequestDto;
import com.catering.dto.tenant.request.DebitNoteResponseDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.DebitNoteService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller responsible for managing debit note operations.
 * This includes creating, updating, retrieving, and deleting debit notes.
 * The controller handles API endpoints for performing CRUD operations on debit notes
 * and provides additional functionality like fetching dropdown data for raw materials
 * and purchase bills related to debit notes.
 *
 * This controller also integrates with the {@link DebitNoteService} to perform the business logic,
 * and uses {@link MessageService} for generating response messages and {@link ExceptionService}
 * for exception handling. The {@link ApiUserRightsConstants} ensures proper authorization for each operation.
 */
@RestController
@RequestMapping(value = ApiPathConstant.DEBIT_NOTE)
@Tag(name = SwaggerConstant.DEBIT_NOTE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DebitNoteController {

	/**
	 * Service responsible for handling application messages.
	 * It is used to retrieve and generate response messages in the controller methods.
	 */
	MessageService messageService;

	/**
	 * Service responsible for handling exceptions.
	 * It is used to throw customized exceptions in case of invalid or bad requests.
	 */
	ExceptionService exceptionService;

	/**
	 * Service responsible for performing business logic related to debit notes.
	 * It handles operations such as creating, updating, retrieving, and deleting debit notes.
	 */
	DebitNoteService debitNoteService;

	/**
	 * Creates a new debit note record.
	 *
	 * @param debitNoteRequestDto The DTO containing the data for the new debit note.
	 * @return A {@link ResponseContainerDto} containing the created debit note.
	 * @throws RestException If an error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<DebitNoteRequestDto>> create(@Valid @RequestBody DebitNoteRequestDto debitNoteRequestDto) {
		Optional<DebitNoteRequestDto> debitNoteRequestDtoList = debitNoteService.createAndUpdate(debitNoteRequestDto);
		return RequestResponseUtils.generateResponseDto(debitNoteRequestDtoList, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing debit note record.
	 *
	 * @param debitNoteRequestDto The DTO containing the updated data for the debit note.
	 * @return A {@link ResponseContainerDto} containing the updated debit note.
	 * @throws RestException If an error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<DebitNoteRequestDto>> update(@Valid @RequestBody DebitNoteRequestDto debitNoteRequestDto) {
		Optional<DebitNoteRequestDto> debitNoteRequestDtoList = debitNoteService.createAndUpdate(debitNoteRequestDto);
		return RequestResponseUtils.generateResponseDto(debitNoteRequestDtoList, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves a list of debit note records based on the provided filter criteria.
	 *
	 * @param filterDto The filter criteria used to search for debit notes.
	 * @return A {@link ResponseContainerDto} containing a list of debit notes matching the filter.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DebitNoteResponseDto>> read(FilterDto filterDto) {
		return debitNoteService.read(filterDto);
	}

	/**
	 * Retrieves a debit note record by its ID.
	 *
	 * @param idStr The ID of the debit note to retrieve.
	 * @return A {@link ResponseContainerDto} containing the debit note with the given ID.
	 * @throws RestException If the ID is invalid or an error occurs during the retrieval.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<DebitNoteGetByIdDto> getById(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) {
		return RequestResponseUtils.generateResponseDto(debitNoteService.getById(Long.parseLong(idStr)));
	}

	/**
	 * Deletes a debit note record by its ID.
	 *
	 * @param idStr The ID of the debit note to delete.
	 * @return A {@link ResponseContainerDto} indicating that the record has been successfully deleted.
	 * @throws RestException If the ID is invalid or an error occurs during deletion.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		if (!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		debitNoteService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of raw materials for the debit note dropdown.
	 * 
	 * @return A {@link ResponseContainerDto} containing a list of raw material dropdown data for the debit note.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_DATA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<DebitNoteRawMaterialDropDownDto>> getRawMaterialDropDownData() {
		return debitNoteService.getRawMaterialDropDownData();
	}

	/**
	 * Retrieves a list of purchase bill data for the debit note dropdown.
	 * 
	 * @param id The ID of the purchase bill to fetch data for.
	 * @return A {@link ResponseContainerDto} containing a list of purchase bill dropdown data.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_BILL_DROPDOWN)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<DebitNotePurchaseBillDropDownDto>> getPurchaseBillDropDownData(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		return debitNoteService.getDebitNotePurchaseBillDropDownData(Objects.nonNull(Long.parseLong(id)) ? Long.parseLong(id) : 0l);
	}

	/**
	 * Retrieves raw material data for a specific purchase bill related to the debit note.
	 * 
	 * @param id The ID of the purchase bill to fetch raw material data for.
	 * @return A {@link ResponseContainerDto} containing raw material data for the purchase bill.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_BILL_RAW_MATERIAL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<PurchaseBillOrderRawMaterialDto>> getPurchaseBillRawMaterial(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		return debitNoteService.getDebitNotePurchaseBillRawMaterial(Long.parseLong(id));
	}

}