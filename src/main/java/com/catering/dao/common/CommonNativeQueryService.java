package com.catering.dao.common;

import java.util.List;

import com.catering.dto.common.VoucherPaymentHistoryDto;
import com.catering.enums.VoucherTypeEnum;

/**
 * Service interface for retrieving voucher payment history details.
 * Provides methods for fetching payment information associated with a specific voucher
 * number and voucher type, including payments made via bank or cash.
 */
public interface CommonNativeQueryService {

	/**
	 * Fetch Payment History via Bank or Cash for particular Voucher Number based on Voucher type
	 *
	 * @param VoucherType {@link VoucherTypeEnum}
	 * @param voucherNumber A unique identifier id
	 * @return List of {@link VoucherPaymentHistoryDto}
	 */
	public List<VoucherPaymentHistoryDto> readVoucherPaymentHistory(int voucherType, long voucherNumber);

}