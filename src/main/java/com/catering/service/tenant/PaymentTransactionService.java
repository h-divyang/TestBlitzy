package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.PaymentTransactionDto;

/**
 * Service interface for managing payment transactions.
 * This interface provides methods for retrieving payment transaction details.
 * It defines a method for fetching all payment transaction records.
 */
public interface PaymentTransactionService {

	/**
	 * Retrieves a list of all payment transactions.
	 *
	 * @return A list of PaymentTransactionDto objects, where each object contains details
	 *		   of a payment transaction such as amount and payment date.
	 */
	List<PaymentTransactionDto> findAll();

}