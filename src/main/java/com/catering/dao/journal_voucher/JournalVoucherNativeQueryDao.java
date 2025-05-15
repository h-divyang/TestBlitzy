package com.catering.dao.journal_voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.JournalVoucherCalculationListDto;

/**
 * Data access object (DAO) for performing database operations related to {@link JournalVoucherNativeQuery}.
 * Extends {@link JpaRepository} to leverage built-in CRUD operations.
 * Provides custom queries to interact with journal voucher-related data using native SQL queries.
 */
public interface JournalVoucherNativeQueryDao extends JpaRepository<JournalVoucherNativeQuery, Long> {

	/**
	 * Retrieves the calculation of a journal voucher, specifically the total Credit (CR) and Debit (DR)
	 * amounts for a given journal voucher ID.
	 * This calculation is based on the native query {@code JournalVoucherCalculation}.
	 * 
	 * @param id The ID of the journal voucher for which the calculation is to be retrieved.
	 * @return A {@link JournalVoucherCalculationListDto} containing the total Credit (CR) and Debit (DR) amounts.
	 */
	@Query(name = "JournalVoucherCalculation", nativeQuery = true)
	JournalVoucherCalculationListDto getCalculationOfJournalVoucher(Long id);

}