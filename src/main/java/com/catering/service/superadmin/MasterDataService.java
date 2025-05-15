package com.catering.service.superadmin;

import java.io.IOException;
import java.util.List;
import com.catering.dto.superadmin.LanguageDto;
import com.catering.dto.superadmin.SubscriptionResponseDto;

/**
 * Service interface for retrieving master data in the system.
 *
 * This interface defines methods for fetching master data such as supported languages and subscription details.
 */
public interface MasterDataService {

	/**
	 * Retrieves a list of languages available in the system.
	 *
	 * @return A list of {@code LanguageDto} objects containing details about the languages such as name, code, and native name.
	 * @throws IOException If there is an input or output exception.
	 * @throws InterruptedException If the thread executing the request is interrupted.
	 */
	List<LanguageDto> getLanguageList() throws IOException, InterruptedException;

	/**
	 * Retrieves a list of subscription details available in the system.
	 *
	 * @return A list of {@code SubscriptionResponseDto} objects containing details about subscriptions such as name, amount, description, user limit, and extra user price.
	 */
	List<SubscriptionResponseDto> getSubscriptionList();

}