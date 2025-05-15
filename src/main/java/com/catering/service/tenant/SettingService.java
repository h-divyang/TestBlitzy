package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.CurrentSubscriptionDto;
import com.catering.dto.tenant.request.PaymentTransactionDto;
import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;
import com.catering.dto.tenant.request.SubscriptionTransactionDto;

/**
 * Service interface for managing settings, providing methods related to setting menus and user rights.
 *
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
public interface SettingService {

	/**
	 * Retrieves a list of setting menus and submenus with user rights for a given user ID.
	 *
	 * @param userId The unique identifier of the user.
	 * @return A list of {@code SettingMenuWithUserRightsDto} representing user rights for setting menus.
	 */
	List<SettingMenuWithUserRightsDto> getSettingMenuWithUserRights(Long userId);

	/**
	 * Retrieves the current subscription details.
	 *
	 * @return A {@code CurrentSubscriptionDto} containing information about the user's active subscription, including plan details,
	 *		   pricing, user count, and subscription dates.
	 */
	CurrentSubscriptionDto getCurrentSubscription();

	/**
	 * Retrieves the payment transaction history.
	 *
	 * @return A list of {@code PaymentTransactionDto} objects representing the payment transaction details,
	 *		   including the amount and payment date for each transaction.
	 */
	List<PaymentTransactionDto> getPaymentHistory();

	/**
	 * Retrieves a list of subscription activity records.
	 *
	 * @return A list of {@code SubscriptionTransactionDto} objects representing subscription activity records.
	 */
	List<SubscriptionTransactionDto> getSubscriptionActivity();

}