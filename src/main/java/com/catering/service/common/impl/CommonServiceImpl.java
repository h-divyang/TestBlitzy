package com.catering.service.common.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.catering.constant.Constants;
import com.catering.dao.common.CommonNativeQueryService;
import com.catering.dto.common.VoucherPaymentHistoryDto;
import com.catering.service.common.CommonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CommonService} interface providing methods for common service operations
 * such as cache management and voucher payment history retrieval.
 *
 * This class interacts with the cache system to handle operations related to clearing specific
 * entries based on unique codes and delegates to the {@link CommonNativeQueryService} to retrieve
 * voucher payment history.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonServiceImpl implements CommonService {

	/**
	 * Handles cache management operations.
	 */
	CacheManager cacheManager;

	/**
	 * Provides services for executing native queries.
	 */
	CommonNativeQueryService commonNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCaching(String uniqueCode) {
		removeEntriesWithUniqueCode(Constants.SIDEBAR_CACHE, uniqueCode);
		removeEntriesWithUniqueCode(Constants.USER_RIGHTS_CACHE, uniqueCode);
		removeEntriesWithUniqueCode(Constants.SIDEBAR_ALL_MENUS_CACHE, uniqueCode);
	}

	/**
	 * Removes entries from a specified cache where the cache key contains a unique code.
	 *
	 * The cache keys are expected to be formatted as strings containing the unique code
	 * delimited by a "-" (dash), e.g., "someKey-uniqueCode". This method identifies keys
	 * where the substring after the dash matches the given unique code and invalidates
	 * those entries from the cache.
	 *
	 * @param cacheName The name of the cache from which entries should be removed.
	 * @param uniqueCode The unique code used to identify the cache entries to be removed.
	 */
	@SuppressWarnings("unchecked")
	private void removeEntriesWithUniqueCode(String cacheName, String uniqueCode) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache.getNativeCache();

			// Use Caffeine's asMap() method to access cache entries and iterate over them
			Set<Object> keysToRemove = nativeCache.asMap().keySet().stream().filter(key -> key instanceof String)
					.filter(key -> {
						String[] parts = ((String) key).split("-");
						return parts.length > 1 && parts[1].equalsIgnoreCase(uniqueCode);
					}).collect(Collectors.toSet());

			// Invalidate all matching keys in one go
			nativeCache.invalidateAll(keysToRemove);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherPaymentHistoryDto> readVoucherPaymentHistory(int voucherType, long voucherNumber) {
		return commonNativeQueryService.readVoucherPaymentHistory(voucherType, voucherNumber);
	}

}