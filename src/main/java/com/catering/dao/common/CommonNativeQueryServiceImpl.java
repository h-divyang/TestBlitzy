package com.catering.dao.common;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.common.VoucherPaymentHistoryDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CommonNativeQueryService} interface that provides methods 
 * for retrieving voucher payment history details.
 * This service interacts with the {@link CommonNativeQueryDao} to fetch payment data for 
 * specific voucher numbers based on voucher type (bank or cash).
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonNativeQueryServiceImpl implements CommonNativeQueryService {

	/**
	 * Data Access Object for interacting with the underlying database 
	 * to fetch voucher payment history data.
	 */
	CommonNativeQueryDao commonNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherPaymentHistoryDto> readVoucherPaymentHistory(int voucherType, long voucherNumber) {
		return commonNativeQueryDao.readVoucherPaymentHistory(voucherType, voucherNumber);
	}

}