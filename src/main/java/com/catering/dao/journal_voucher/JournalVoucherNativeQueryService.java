package com.catering.dao.journal_voucher;

import com.catering.dto.tenant.request.JournalVoucherCalculationListDto;

/**
 * Service interface for performing operations related to {@link JournalVoucherNativeQuery}.
 * Provides methods for retrieving calculations of journal vouchers, such as the total Credit (CR) 
 * and Debit (DR) amounts for a specific journal voucher.
 */
public interface JournalVoucherNativeQueryService {

	/**
	 * Retrieves the calculation of a journal voucher, specifically the total Credit (CR) and Debit (DR)
	 * amounts for a given journal voucher ID.
	 * 
	 * @param id The ID of the journal voucher for which the calculation is to be retrieved.
	 * @return A {@link JournalVoucherCalculationListDto} containing the total Credit (CR) and Debit (DR) amounts.
	 */
	JournalVoucherCalculationListDto getCalculationOfJournalVoucher(Long id);

}