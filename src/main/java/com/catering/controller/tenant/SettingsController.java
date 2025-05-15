package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CurrentSubscriptionDto;
import com.catering.dto.tenant.request.PaymentTransactionDto;
import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;
import com.catering.dto.tenant.request.SubscriptionTransactionDto;
import com.catering.service.tenant.SettingService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for handling requests related to settings, providing endpoints for managing setting menus and user rights.
 *
 * Endpoints:
 *   - GET /settings/{id} : Retrieve a list of setting menus and submenus with user rights for a given user ID.
 * 
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
@RestController
@RequestMapping(value = ApiPathConstant.SETTINGS)
@Tag(name = SwaggerConstant.SETTINGS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingsController {

	/**
	 * Autowired instance of {@code SettingService} for handling setting-related business logic.
	 */
	SettingService settingService;

	/**
	 * GET endpoint to retrieve a list of setting menus and submenus with user rights for a given user ID.
	 *
	 * @param userId The unique identifier of the user.
	 * @return A response container containing a list of {@code SettingMenuWithUserRightsDto}.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	public ResponseContainerDto<List<SettingMenuWithUserRightsDto>> getSettingMenuWithSubMenuAndUserRights(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String userId) {
		return RequestResponseUtils.generateResponseDto(settingService.getSettingMenuWithUserRights(Long.parseLong(userId)));
	}

	/**
	 * Retrieves the current subscription details.
	 * This method returns the details of the current subscription from the system settings.
	 *
	 * @return a {@link ResponseContainerDto} containing the current subscription details as a {@link CurrentSubscriptionDto}.
	 */
	@GetMapping(value = ApiPathConstant.CURRENT_SUBSCRIPTION)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.SUBSCRIPTION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<CurrentSubscriptionDto> getCurrentSubscription() {
		return RequestResponseUtils.generateResponseDto(settingService.getCurrentSubscription());
	}

	/**
	 * Retrieves the payment history.
	 * This method returns the list of payment transactions made for subscriptions.
	 *
	 * @return a {@link ResponseContainerDto} containing a list of {@link PaymentTransactionDto} representing the payment history.
	 */
	@GetMapping(value = ApiPathConstant.PAYMENT_HISTORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PAYMENT_HISTORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<PaymentTransactionDto>> getPaymentHistory() {
		return RequestResponseUtils.generateResponseDto(settingService.getPaymentHistory());
	}

	/**
	 * Retrieves the subscription activity.
	 * This method returns the list of subscription transactions, such as activations, renewals, or cancellations.
	 *
	 * @return a {@link ResponseContainerDto} containing a list of {@link SubscriptionTransactionDto} representing the subscription activity.
	 */
	@GetMapping(value = ApiPathConstant.SUBSCRIPTION_ACTIVITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.SUBSCRIPTION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<SubscriptionTransactionDto>> getSubscriptionActivity() {
		return RequestResponseUtils.generateResponseDto(settingService.getSubscriptionActivity());
	}

}