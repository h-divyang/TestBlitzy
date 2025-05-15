package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.BankPaymentReceiptRequestDto;
import com.catering.dto.tenant.request.BankPaymentReceiptResponseDto;
import com.catering.exception.RestException;

/**
 * Service interface for managing bank payment receipts, including creation, updating,
 * deletion, and retrieval of receipts.
 */
public interface BankPaymentReceiptService {

	/**
	 * Reads a list of bank payment receipt details based on the provided filter criteria.
	 *
	 * @param filterDto An object containing the filtering criteria for retrieving bank payment receipts.
	 * @param voucherType A boolean indicating the type of voucher to filter.
	 * @return A ResponseContainerDto containing a list of BankPaymentReceiptResponseDto objects and additional metadata.
	 */
	ResponseContainerDto<List<BankPaymentReceiptResponseDto>> read(FilterDto filterDto, boolean voucherType);

	/**
	 * Creates or updates a bank payment receipt based on the provided data.
	 *
	 * @param bankPaymentReceiptRequestDto The BankPaymentReceiptRequestDto containing the details of the bank payment receipt to be created or updated.
	 * @return An Optional containing the updated or newly created BankPaymentReceiptRequestDto, or an empty Optional if the operation fails.
	 * @throws RestException If there is an issue during the creation or update process.
	 */
	Optional<BankPaymentReceiptRequestDto> createUpdateBankPaymentReceipt(BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) throws RestException;

	/**
	 * Deletes a bank payment/receipt by its unique identifier.
	 *
	 * @param id The unique identifier of the bank payment/receipt to be deleted.
	 */
	void deleteBankPaymentReceipt(Long id);

	/**
	 * Retrieves a bank payment receipt based on the provided unique identifier.
	 *
	 * @param id The unique identifier of the bank payment receipt to be retrieved.
	 * @return An Optional containing the BankPaymentReceiptRequestDto if found, or an empty Optional if no receipt exists for the given id.
	 * @throws RestException If there is an issue while retrieving the bank payment receipt.
	 */
	Optional<BankPaymentReceiptRequestDto> getBankPaymentReceipt(Long id) throws RestException;

}