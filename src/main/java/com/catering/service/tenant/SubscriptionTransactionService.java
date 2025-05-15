package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.SubscriptionTransactionDto;

/**
 * Service interface for managing subscription transaction operations.
 *
 * This interface defines methods to interact with subscription-related transaction data.
 * It provides functionality to retrieve logs or records of subscription activities. Implementations of this
 * interface will provide the concrete logic for interacting with the subscription transaction data.
 */
public interface SubscriptionTransactionService {

	/**
	 * Retrieves the subscription activity log.
	 *
	 * This method fetches a list of subscription transaction details,
	 * containing information about various subscription activities such as start date, end date, subscription type,
	 * and other relevant details.
	 *
	 * @return A list of SubscriptionTransactionDto objects representing the subscription activity records.
	 */
	List<SubscriptionTransactionDto> getSubscriptionActivity();

}