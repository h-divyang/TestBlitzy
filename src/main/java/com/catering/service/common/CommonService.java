package com.catering.service.common;

import java.util.List;
import com.catering.dto.common.VoucherPaymentHistoryDto;

/**
 * Provides common service operations such as cache management and voucher payment history retrieval.
 */
public interface CommonService {

	/**
	 * Removes entries from the specified cache that are associated with the current
	 * login company and match the provided uniqueCode.
	 *
	 * @param uniqueCode A unique identifier used to filter and remove specific cache entries.
	 * @throws IllegalArgumentException if the uniqueCode is null or empty.
	 */
	void updateCaching(String uniqueCode);

	/**
	 * Retrieves the payment history for a specified voucher based on the given voucher type and voucher number.
	 *
	 * @param voucherType The type of the voucher to retrieve payment history for.
	 * @param voucherNumber The unique number of the voucher to query.
	 * @return A list of VoucherPaymentHistoryDto objects representing the payment history of the specified voucher.
	 */
	List<VoucherPaymentHistoryDto> readVoucherPaymentHistory(int voucherType, long voucherNumber);

}