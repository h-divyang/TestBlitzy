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
import com.catering.dto.tenant.request.BankPaymentReceiptRequestDto;
import com.catering.dto.tenant.request.BankPaymentReceiptResponseDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.BankPaymentReceiptService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller responsible for handling REST API end points related to bank payment and receipt operations.
 * This controller provides end points for creating, reading, updating, and deleting bank payment and receipt records.
 */
@RestController
@RequestMapping(value = ApiPathConstant.BANK_PAYMENT_RECEIPT)
@Tag(name = SwaggerConstant.BANK_PAYMENT_RECEIPT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BankPaymentReceiptController {

	/**
	 * Service responsible for obtaining localized messages.
	 */
	MessageService messageService;

	/**
	 * Service used for handling and throwing custom exceptions within the application.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for handling business logic related to bank payments and receipts.
	 *
	 * This service provides methods for creating, updating, retrieving, and deleting bank payment and receipt records.
	 */
	BankPaymentReceiptService bankPaymentReceiptService;

	/**
	 * Retrieves a list of bank payment records based on the provided filter criteria.
	 *
	 * @param filterDto The filter parameters for pagination and sorting.
	 * @return A container object wrapping a list of bank payment response details along with metadata.
	 */
	@GetMapping(value = ApiPathConstant.BANK_PAYMENT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<BankPaymentReceiptResponseDto>> readBankPayment(FilterDto filterDto) {
		return bankPaymentReceiptService.read(filterDto, false);
	}

	/**
	 * Retrieves a list of bank receipt records based on the provided filter criteria.
	 *
	 * @param filterDto The filter parameters for pagination and sorting.
	 * @return A container object wrapping a list of bank receipt response details along with metadata.
	 */
	@GetMapping(value = ApiPathConstant.BANK_RECEIPT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<BankPaymentReceiptResponseDto>> readBankReceipt(FilterDto filterDto) {
		return bankPaymentReceiptService.read(filterDto, true);
	}

	/**
	 * Creates a new bank payment record based on the provided details.
	 * The method performs necessary validations and invokes the service to persist the data.
	 *
	 * @param bankPaymentReceiptRequestDto The details of the bank payment or receipt to be created.
	 * @return A container object wrapping an optional BankPaymentReceiptRequestDto, representing the created record.
	 */
	@PostMapping(value = ApiPathConstant.BANK_PAYMENT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<BankPaymentReceiptRequestDto>> createBankPayment(@Valid @RequestBody BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) {
		Optional<BankPaymentReceiptRequestDto> bankPaymentReceipt = bankPaymentReceiptService.createUpdateBankPaymentReceipt(bankPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(bankPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Creates a new bank receipt record based on the provided details.
	 * The method performs necessary validations and invokes the service to persist the data.
	 *
	 * @param bankPaymentReceiptRequestDto The details of the bank payment or receipt to be created.
	 * @return A container object wrapping an optional BankPaymentReceiptRequestDto, representing the created record.
	 */
	@PostMapping(value = ApiPathConstant.BANK_RECEIPT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<BankPaymentReceiptRequestDto>> createBankReceipt(@Valid @RequestBody BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) {
		Optional<BankPaymentReceiptRequestDto> bankPaymentReceipt = bankPaymentReceiptService.createUpdateBankPaymentReceipt(bankPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(bankPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing bank payment record with the provided details.
	 * The method performs necessary validations and invokes the service to update the data.
	 *
	 * @param bankPaymentReceiptRequestDto The details of the bank payment or receipt to be updated.
	 * @return A container object wrapping an optional BankPaymentReceiptRequestDto, representing the updated record if the update was successful.
	 */
	@PutMapping(value = ApiPathConstant.BANK_PAYMENT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<BankPaymentReceiptRequestDto>> updateBankPayment(@Valid @RequestBody BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) {
		Optional<BankPaymentReceiptRequestDto> bankPaymentReceipt = bankPaymentReceiptService.createUpdateBankPaymentReceipt(bankPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(bankPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates an existing bank receipt record with the provided details.
	 * The method performs necessary validations and invokes the service to update the data.
	 *
	 * @param bankPaymentReceiptRequestDto The details of the bank payment or receipt to be updated.
	 * @return A container object wrapping an optional BankPaymentReceiptRequestDto, representing the updated record if the update was successful.
	 */
	@PutMapping(value = ApiPathConstant.BANK_RECEIPT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<BankPaymentReceiptRequestDto>> updateBankReceipt(@Valid @RequestBody BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) {
		Optional<BankPaymentReceiptRequestDto> bankPaymentReceipt = bankPaymentReceiptService.createUpdateBankPaymentReceipt(bankPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(bankPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves a bank payment or receipt record based on the provided ID.
	 * This method performs validation on the ID and invokes the service to fetch the corresponding bank payment or receipt details.
	 *
	 * @param id The unique identifier of the bank payment or receipt record.
	 * @return A container object wrapping an optional BankPaymentReceiptRequestDto, representing the bank payment or receipt details if found.
	 * @throws RestException If any error occurs during the retrieval process.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<Optional<BankPaymentReceiptRequestDto>> getbankPaymentReceipt(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) throws RestException {
		return RequestResponseUtils.generateResponseDto(bankPaymentReceiptService.getBankPaymentReceipt(Long.valueOf(id)));
	}

	/**
	 * Deletes a bank payment or receipt record based on the provided ID.
	 * This method validates the provided ID, ensuring it is numeric, and invokes the service to delete the corresponding record.
	 *
	 * @param id The unique identifier of the bank payment or receipt record to be deleted.
	 * @return A container object wrapping a response message confirming the deletion.
	 * @throws BadRequestException If the provided ID is not valid or not numeric.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_DELETE, ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_DELETE}, checkAll = false)
	public ResponseContainerDto<Object> deletebankPaymentReceipt(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		if(!ValidationUtils.isNumber(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		bankPaymentReceiptService.deleteBankPaymentReceipt(Long.valueOf(id));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}