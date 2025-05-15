package com.catering.dao.journal_voucher;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.JournalVoucherCalculationListDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for performing operations related to {@link JournalVoucherNativeQuery}.
 * This class implements the {@link JournalVoucherNativeQueryService} interface and provides methods
 * to interact with journal voucher data, such as retrieving the calculation of a journal voucher's
 * total Credit (CR) and Debit (DR) amounts.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JournalVoucherNativeQueryServiceImpl implements JournalVoucherNativeQueryService {

	/**
	 * DAO for interacting with the {@link JournalVoucherNativeQuery} entity.
	 * Provides methods for executing native SQL queries related to journal vouchers.
	 */
	JournalVoucherNativeQueryDao journalVoucherNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JournalVoucherCalculationListDto getCalculationOfJournalVoucher(Long id) {
		return journalVoucherNativeQueryDao.getCalculationOfJournalVoucher(id);
	}

}