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
import com.catering.dto.tenant.request.CashPaymentReceiptRequestDto;
import com.catering.dto.tenant.request.CashPaymentReceiptResponseDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CashPaymentReceiptService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling cash payment and receipt operations.
 * Provides end points for creating, updating, retrieving, and deleting cash payment or receipt records.
 * Additionally, it manages voucher number and payment contact-related operations.
 */
@RestController
@RequestMapping(value = ApiPathConstant.CASH_PAYMENT_RECEIPT)
@Tag(name = SwaggerConstant.CASH_PAYMENT_RECEIPT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CashPaymentReceiptController {

	/**
	 * Service for handling messages or notifications within the application.
	 */
	MessageService messageService;

	/**
	 * Service for managing exceptions and error handling.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for handling operations related to cash payment receipts.
	 */
	CashPaymentReceiptService cashPaymentReceiptService;

	/**
	 * Retrieves a list of cash payment details based on the provided filter and transaction type.
	 *
	 * @param filterDto The filter criteria used to query the records.
	 * @return A {@code ResponseContainerDto} containing a list of {@code CashPaymentReceiptResponseDto} that match the criteria.
	 */
	@GetMapping(value = ApiPathConstant.CASH_PAYMENT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CashPaymentReceiptResponseDto>> readCashPayment(FilterDto filterDto) {
		return cashPaymentReceiptService.read(filterDto, false);
	}

	/**
	 * Retrieves a list of cash payment details based on the provided filter and transaction type.
	 *
	 * @param filterDto The filter criteria used to query the records.
	 * @return A {@code ResponseContainerDto} containing a list of {@code CashPaymentReceiptResponseDto} that match the criteria.
	 */
	@GetMapping(value = ApiPathConstant.CASH_RECEIPT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CashPaymentReceiptResponseDto>> readCashReceipt(FilterDto filterDto) {
		return cashPaymentReceiptService.read(filterDto, true);
	}

	/**
	 * Creates a new cash payment entry based on the provided details.
	 *
	 * @param cashPaymentReceiptRequestDto The details of the cash payment or receipt to be created, wrapped in a {@code CashPaymentReceiptRequestDto} object
	 * @return A {@code ResponseContainerDto} wrapping an {@code Optional<CashPaymentReceiptRequestDto>} representing the created cash payment or receipt.
	 */
	@PostMapping(value = ApiPathConstant.CASH_PAYMENT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<CashPaymentReceiptRequestDto>> createCashPayment(@Valid @RequestBody CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) {
		Optional<CashPaymentReceiptRequestDto> cashPaymentReceipt = cashPaymentReceiptService.createUpdateCashPaymentReceipt(cashPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(cashPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Creates a new cash receipt entry based on the provided details.
	 *
	 * @param cashPaymentReceiptRequestDto The details of the cash payment or receipt to be created, wrapped in a {@code CashPaymentReceiptRequestDto} object
	 * @return A {@code ResponseContainerDto} wrapping an {@code Optional<CashPaymentReceiptRequestDto>} representing the created cash payment or receipt.
	 */
	@PostMapping(value = ApiPathConstant.CASH_RECEIPT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<CashPaymentReceiptRequestDto>> createCashReceipt(@Valid @RequestBody CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) {
		Optional<CashPaymentReceiptRequestDto> cashPaymentReceipt = cashPaymentReceiptService.createUpdateCashPaymentReceipt(cashPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(cashPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing cash payment entry based on the provided details.
	 *
	 * @param cashPaymentReceiptRequestDto The details of the cash payment or receipt to be updated.
	 * @return A {@code ResponseContainerDto} wrapping an {@code Optional<CashPaymentReceiptRequestDto>} that represents the updated cash payment or receipt.
	 */
	@PutMapping(value = ApiPathConstant.CASH_PAYMENT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<CashPaymentReceiptRequestDto>> updateCashPayment(@Valid @RequestBody CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) {
		Optional<CashPaymentReceiptRequestDto> cashPaymentReceipt = cashPaymentReceiptService.createUpdateCashPaymentReceipt(cashPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(cashPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates an existing cash receipt entry based on the provided details.
	 *
	 * @param cashPaymentReceiptRequestDto The details of the cash payment or receipt to be updated.
	 * @return A {@code ResponseContainerDto} wrapping an {@code Optional<CashPaymentReceiptRequestDto>} that represents the updated cash payment or receipt.
	 */
	@PutMapping(value = ApiPathConstant.CASH_RECEIPT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<CashPaymentReceiptRequestDto>> updateCashReceipt(@Valid @RequestBody CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) {
		Optional<CashPaymentReceiptRequestDto> cashPaymentReceipt = cashPaymentReceiptService.createUpdateCashPaymentReceipt(cashPaymentReceiptRequestDto);
		return RequestResponseUtils.generateResponseDto(cashPaymentReceipt, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the details of a cash payment or receipt based on the provided ID.
	 *
	 * @param id The identifier of the cash payment or receipt. It must be a numeric string without spaces.
	 * @return A {@code ResponseContainerDto} wrapping an {@code Optional<CashPaymentReceiptRequestDto>} that
	 *		   contains the details of the requested cash payment or receipt, if found.
	 * @throws RestException If any exception occurs during the retrieval process.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<Optional<CashPaymentReceiptRequestDto>> getCashPaymentReceipt(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) throws RestException {
		return RequestResponseUtils.generateResponseDto(cashPaymentReceiptService.getCashPaymentReceipt(Long.valueOf(id)));
	}

	/**
	 * Deletes a cash payment or cash receipt record based on its unique identifier.
	 *
	 * @param id The unique identifier of the cash payment or receipt record to be deleted.
	 * @return A {@code ResponseContainerDto} containing a confirmation message of the deletion.
	 * @throws BadRequestException If the provided ID is not a valid numeric string.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_DELETE, ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_DELETE}, checkAll = false)
	public ResponseContainerDto<Object> deleteCashPaymentReceipt(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		if(!ValidationUtils.isNumber(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		cashPaymentReceiptService.deleteCashPaymentReceipt(Long.valueOf(id));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of voucher numbers associated with a given ID.
	 *
	 * @param id The identifier used to fetch the associated voucher numbers. It must be a numeric string.
	 * @return A {@code ResponseContainerDto} containing a list of {@code VoucherNumberDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.VOUCHER_NO_LIST + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<VoucherNumberDto>> getVoucherNumber(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		return RequestResponseUtils.generateResponseDto(cashPaymentReceiptService.getVoucherNumber(Integer.valueOf(id)));
	}

	/**
	 * Retrieves a list of voucher-type contacts based on the provided identifier.
	 *
	 * @param id The identifier used to fetch the list of voucher-type contacts. It must be a numeric string.
	 * @return A ResponseContainerDto containing a list of PaymentContactCustomDto objects.
	 */
	@GetMapping(value = ApiPathConstant.PAYMENT_CONTACT_LIST + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<PaymentContactCustomDto>> getVoucherTypeContactList(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		return RequestResponseUtils.generateResponseDto(cashPaymentReceiptService.fetchVoucherTypeContactList(Integer.valueOf(id)));
	}

}