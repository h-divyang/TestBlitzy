package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.CashPaymentReceiptRequestDto;
import com.catering.dto.tenant.request.CashPaymentReceiptResponseDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;
import com.catering.exception.RestException;

/**
 * Interface for managing operations related to Cash Payment Receipts, including creation,
 * retrieval, update, deletion, and associated functionality such as voucher and contact management.
 */
public interface CashPaymentReceiptService {

	/**
	 * Reads a list of cash payment receipts based on the provided filter criteria and voucher type.
	 *
	 * @param filterDto The filter criteria used to fetch the list of cash payment receipts, including pagination and sorting details.
	 * @param voucherType A boolean flag to specify the type of voucher.
	 * @return A ResponseContainerDto containing the list of CashPaymentReceiptResponseDto objects and metadata such as status and message.
	 */
	ResponseContainerDto<List<CashPaymentReceiptResponseDto>> read(FilterDto filterDto, boolean voucherType);

	/**
	 * Creates or updates a cash payment receipt based on the provided data.
	 *
	 * @param cashPaymentReceiptRequestDto The data transfer object containing the details of the cash payment receipt to be created or updated.
	 * @return An Optional containing the created or updated CashPaymentReceiptRequestDto or empty if the operation fails.
	 * @throws RestException If an error occurs during the process, such as validation issues or unexpected server errors.
	 */
	Optional<CashPaymentReceiptRequestDto> createUpdateCashPaymentReceipt(CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) throws RestException;

	/**
	 * Deletes a specific cash payment receipt by its unique identifier.
	 *
	 * @param id The unique identifier of the cash payment receipt to be deleted.
	 */
	void deleteCashPaymentReceipt(Long id);

	/**
	 * Retrieves a specific cash payment receipt based on its unique identifier.
	 *
	 * @param id The unique identifier of the cash payment receipt to retrieve.
	 * @return An Optional containing the CashPaymentReceiptRequestDto if found, or an empty Optional if not found.
	 * @throws RestException If an error occurs during the retrieval process, such as the receipt not being found or unexpected server errors.
	 */
	Optional<CashPaymentReceiptRequestDto> getCashPaymentReceipt(Long id) throws RestException;

	/**
	 * Retrieves a list of voucher numbers based on the specified voucher type.
	 *
	 * @param voucherType An integer representing the type of voucher for which the voucher numbers are to be fetched.
	 * @return A list of {@code VoucherNumberDto} objects.
	 */
	List<VoucherNumberDto> getVoucherNumber(int voucherType);

	/**
	 * Retrieves a list of PaymentContactCustomDto objects associated with a specific voucher type.
	 *
	 * @param voucherType An integer representing the type of voucher for which the contacts are to be fetched.
	 * @return A list of PaymentContactCustomDto objects related to the specified voucher type.
	 */
	List<PaymentContactCustomDto> fetchVoucherTypeContactList(int voucherType);

	/**
	 * Determines if a specific entity is being used in either Cash or Bank operations based on the provided identifier and voucher type IDs.
	 *
	 * @param id The unique identifier of the entity to check for usage in Cash or Bank operations.
	 * @param voucherTypeIds A list of voucher type IDs to consider when checking for usage.
	 * @return An Integer value indicating the usage status in Cash or Bank operations.
	 */
	Integer isUseInCashOrBank(long id, List<Integer> voucherTypeIds);

}