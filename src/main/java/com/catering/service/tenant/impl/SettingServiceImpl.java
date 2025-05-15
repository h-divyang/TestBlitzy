package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import com.catering.dao.setting.SettingNativeQueryService;
import com.catering.dto.superadmin.SubscriptionResponseDto;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.CurrentSubscriptionDto;
import com.catering.dto.tenant.request.PaymentTransactionDto;
import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;
import com.catering.dto.tenant.request.SubscriptionTransactionDto;
import com.catering.service.superadmin.MasterDataService;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.PaymentTransactionService;
import com.catering.service.tenant.SettingService;
import com.catering.service.tenant.SubscriptionTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing settings, providing methods related to setting menus and user rights.
 * This class delegates the retrieval of setting menus and user rights to {@code SettingNativeQueryService}.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @see SettingService
 * @see SettingNativeQueryService
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingServiceImpl implements SettingService {

	/**
	 * Service for executing native queries related to settings.
	 */
	SettingNativeQueryService settingNativeQueryService;

	/**
	 * Service for managing company preferences and settings.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Service for managing master data records.
	 */
	MasterDataService masterDataService;

	/**
	 * Service for handling payment transactions.
	 */
	PaymentTransactionService paymentTransactionService;

	/**
	 * Service for managing subscription transactions.
	 */
	SubscriptionTransactionService subscriptionTransactionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SettingMenuWithUserRightsDto> getSettingMenuWithUserRights(Long userId) {
		return settingNativeQueryService.getSettingMenuAndSubMenuWithUserRights(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CurrentSubscriptionDto getCurrentSubscription() {
		CurrentSubscriptionDto currentSubscriptionDto = new CurrentSubscriptionDto();
		CompanyPreferencesDto companyPreferences = companyPreferencesService.find().get();
		currentSubscriptionDto.setTotalUsers(companyPreferences.getUsers());
		currentSubscriptionDto.setActiveDate(companyPreferences.getActiveDate());
		currentSubscriptionDto.setDueDate(companyPreferences.getDueDate());
		currentSubscriptionDto.setSubscriptionType(companyPreferences.getSubscriptionType());
		if (Objects.nonNull(companyPreferences.getSubscriptionId())) {
			List<SubscriptionResponseDto> currentSubscriptionDtoList = masterDataService.getSubscriptionList();
			SubscriptionResponseDto subscriptionResponse = currentSubscriptionDtoList.stream().filter(subscriptionResponseDto -> subscriptionResponseDto.getId().equals(companyPreferences.getSubscriptionId())).findFirst().get();
			currentSubscriptionDto.setSubscriptionId(companyPreferences.getSubscriptionId());
			currentSubscriptionDto.setPlan(subscriptionResponse.getName());
			currentSubscriptionDto.setPlanPrice(subscriptionResponse.getAmount().floatValue());
			currentSubscriptionDto.setTotalPrice(subscriptionResponse.getAmount().floatValue());
			currentSubscriptionDto.setPlanUsers(companyPreferences.getUsers());
			if (Objects.nonNull(companyPreferences.getExtraUsers())) {
				currentSubscriptionDto.setExtraUsers(companyPreferences.getExtraUsers());
				currentSubscriptionDto.setExtraUsersPrice(subscriptionResponse.getExtraUserPrice().floatValue());
				currentSubscriptionDto.setTotalExtraUsersPrice(companyPreferences.getExtraUsers() * subscriptionResponse.getExtraUserPrice().floatValue());
				currentSubscriptionDto.setTotalUsers(companyPreferences.getUsers() + companyPreferences.getExtraUsers());
				currentSubscriptionDto.setTotalPrice((float) (subscriptionResponse.getAmount() + (subscriptionResponse.getExtraUserPrice() * companyPreferences.getExtraUsers())));
			}
		}
		return currentSubscriptionDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentTransactionDto> getPaymentHistory() {
		return paymentTransactionService.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriptionTransactionDto> getSubscriptionActivity() {
		return subscriptionTransactionService.getSubscriptionActivity();
	}

}