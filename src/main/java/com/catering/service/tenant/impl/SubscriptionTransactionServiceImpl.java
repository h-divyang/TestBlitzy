package com.catering.service.tenant.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.catering.dto.superadmin.SubscriptionResponseDto;
import com.catering.dto.tenant.request.SubscriptionTransactionDto;
import com.catering.enums.SubscriptionTransactionTypeEnum;
import com.catering.repository.tenant.SubscriptionTransactionRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.superadmin.MasterDataService;
import com.catering.service.tenant.SubscriptionTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the SubscriptionTransactionService interface for managing
 * subscription transaction operations.
 *
 * This service is responsible for retrieving and processing subscription activities,
 * such as transaction logs, with details including subscription type changes and updates.
 * It also maps relevant entity data to DTOs for use in other layers of the application.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionTransactionServiceImpl implements SubscriptionTransactionService {

	/**
	 * Repository for managing subscription transactions.
	 */
	SubscriptionTransactionRepository subscriptionTransactionRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for managing master data records.
	 */
	MasterDataService masterDataService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriptionTransactionDto> getSubscriptionActivity() {
		List<SubscriptionTransactionDto> subscriptionTransactionList = modelMapperService.convertListEntityAndListDto(subscriptionTransactionRepository.findAllByOrderByIdDesc(), SubscriptionTransactionDto.class);
		List<SubscriptionResponseDto> currentSubscriptionDtoList = masterDataService.getSubscriptionList();
		subscriptionTransactionList.forEach(subscriptionTransaction -> {
			String updatedRecord = switch (SubscriptionTransactionTypeEnum.of(subscriptionTransaction.getSubscriptionType())) {
				case MODULE_SWITCH -> getPlanName(subscriptionTransaction.getSubscriptionId(), subscriptionTransaction.getSubscriptionIdPrevious(),currentSubscriptionDtoList);
				default -> "-";
			};
			subscriptionTransaction.setUpdatedRecord(updatedRecord);
		});
		return subscriptionTransactionList;
	}

	/**
	 * Retrieves the concatenated plan names of the current and previous subscriptions
	 * based on their IDs from a list of SubscriptionResponseDto.
	 *
	 * @param subscriptionId The ID of the current subscription.
	 * @param subscriptionIdPrevious The ID of the previous subscription.
	 * @param subscriptionList The list of SubscriptionResponseDto objects containing subscription details.
	 * @return A String representing the concatenated plan names in the format "previousPlanName -> currentPlanName",
	 *		   or an empty string if no matching plan names are found.
	 */
	private String getPlanName(Long subscriptionId, Long subscriptionIdPrevious, List<SubscriptionResponseDto> subscriptionList) {
		String previousPlanName = subscriptionList.stream().filter(subscription -> subscription.getId().equals(subscriptionIdPrevious)).map(SubscriptionResponseDto::getName).findFirst().orElse(null);
		String currentPlanName = subscriptionList.stream().filter(subscription -> subscription.getId().equals(subscriptionId)).map(SubscriptionResponseDto::getName).findFirst().orElse(null);
		return String.join(" -> ", previousPlanName == null ? "" : previousPlanName, currentPlanName == null ? "" : currentPlanName).trim();
	}

}