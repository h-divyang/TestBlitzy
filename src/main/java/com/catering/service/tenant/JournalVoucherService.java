package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.JournalVoucherDto;
import com.catering.dto.tenant.request.JournalVoucherResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.JournalVoucherModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing and performing operations related to Journal Vouchers.
 * Extends the GenericService interface for CRUD operations and additional query methods.
 */
public interface JournalVoucherService extends GenericService<JournalVoucherResponseDto, JournalVoucherModel, Long> {

	/**
	 * Creates or updates a journal voucher based on the provided JournalVoucherDto.
	 *
	 * @param journalVoucherDto A JournalVoucherDto containing the details of the journal voucher to be created or updated.
	 * @return An Optional containing the created or updated JournalVoucherDto, or an empty Optional if the operation failed.
	 * @throws RestException If there is an error during the creation or update process.
	 */
	Optional<JournalVoucherDto> createAndUpdate(JournalVoucherDto journalVoucherDto) throws RestException;

	/**
	 * Retrieves a list of journal vouchers based on the provided filter criteria.
	 *
	 * @param filterDto The filter criteria for retrieving journal vouchers. It may contain paging, sorting, and search parameters.
	 * @return A `ResponseContainerDto` containing a list of `JournalVoucherResponseDto` and additional metadata about the response.
	 */
	ResponseContainerDto<List<JournalVoucherResponseDto>> getAllJournalVoucher(FilterDto filterDto);

	/**
	 * Deletes a journal voucher identified by the given ID.
	 *
	 * @param id The unique identifier of the journal voucher to be deleted.
	 */
	void deleteJournalVoucher(Long id);

	/**
	 * Retrieves a Journal Voucher based on the given ID.
	 *
	 * @param id The unique identifier of the Journal Voucher to be retrieved.
	 * @return An Optional containing the JournalVoucherDto if found, or an empty Optional if no Journal Voucher exists with the provided ID.
	 * @throws RestException If there is an error while retrieving the Journal Voucher.
	 */
	Optional<JournalVoucherDto> getJournalVoucher(Long id) throws RestException;

}