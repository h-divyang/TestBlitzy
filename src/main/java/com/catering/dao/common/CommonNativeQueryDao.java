package com.catering.dao.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.dto.common.VoucherPaymentHistoryDto;

/**
 * DAO interface for querying voucher payment history.
 * Provides access to custom native queries related to payment history, specifically 
 * fetching bank and cash payment details based on voucher type and voucher number.
 */
public interface CommonNativeQueryDao extends JpaRepository<CommonNativeQuery, Long> {

	/**
	 * Retrieves the voucher payment history for a given voucher type and voucher number.
	 * This query fetches the payment details from both bank and cash payment receipt tables
	 * and orders the results by transaction date in descending order.
	 *
	 * @param voucherType The type of the voucher (bank or cash).
	 * @param voucherNumber The unique identifier of the voucher.
	 * @return A list of {@link VoucherPaymentHistoryDto} containing the payment history details.
	 */
	@Query(name = "readVoucherPaymentHistory", nativeQuery = true)
	List<VoucherPaymentHistoryDto> readVoucherPaymentHistory(@Param("voucherType") Integer voucherType, @Param("voucherNumber") Long voucherNumber);

}