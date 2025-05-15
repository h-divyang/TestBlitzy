package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.catering.constant.Constants;
import com.catering.dao.report_user_rights.ReportUserRightsNativeQueryService;
import com.catering.dto.tenant.ReportUserRightsDto;
import com.catering.dto.tenant.ReportUserRightsWithUsersDto;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.model.tenant.ReportUserRightsModel;
import com.catering.repository.tenant.CompanyUserRepository;
import com.catering.repository.tenant.ReportUserRightsRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.ReportUserRightsService;
import com.catering.util.DataUtils;
import java.util.Collections;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing report user rights.
 * This class provides functionalities for retrieving, saving, and updating report user rights
 * along with handling caching mechanisms for performance optimization.
 * 
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 * <li>Fetch all report user rights and map them to DTOs.</li>
 * <li>Save or update report user rights based on provided DTO data.</li>
 * <li>Manage cache entries related to report rights for improved performance.</li>
 * </ul>
 * 
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportUserRightsServiceImpl extends GenericServiceImpl<ReportUserRightsDto, ReportUserRightsModel, Long> implements ReportUserRightsService {

	/**
	 * Service for entity-DTO conversion.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for accessing company user data.
	 */
	CompanyUserRepository companyUserRepository;

	/**
	 * Service for executing native queries for report user rights.
	 */
	ReportUserRightsNativeQueryService reportUserRightsNativeQueryService;

	/**
	 * Repository for accessing report rights data.
	 */
	ReportUserRightsRepository reportUserRightsRepository;

	/**
	 * Cache manager for handling report rights cache entries.
	 */
	CacheManager cacheManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportUserRightsWithUsersDto> getReportUserRights() {
		List<CompanyUserModel> companyUserModels = companyUserRepository.findAllByIsActiveOrderByIdAsc(true);
		List<ReportUserRightsWithUsersDto> reportUserRightsDtos = modelMapperService.convertListEntityAndListDto(companyUserModels, ReportUserRightsWithUsersDto.class);
		reportUserRightsDtos.forEach(user -> user.setReportUserRightsReportCategoryDtoList(reportUserRightsNativeQueryService.reportUserRightsReportCategoryDtoList(user.getId())));
		return reportUserRightsDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportUserRightsDto> saveOrUpdateReportUserRight(List<ReportUserRightsDto> reportUserRightsDtos, String uniqueCode) {
		// Convert sub-menu items to UserRightsModel instances and save
		List<ReportUserRightsModel> reportUserRightsModels = modelMapperService.convertListEntityAndListDto(reportUserRightsDtos, ReportUserRightsModel.class);
		reportUserRightsModels.forEach(reportUserRight -> DataUtils.setAuditFields(reportUserRightsRepository, reportUserRight.getId(), reportUserRight));
		reportUserRightsModels.forEach(reportUserRight -> {
			if (Objects.isNull(reportUserRight.getId())) {
				reportUserRight.setIsActive(true);
				reportUserRight.setEditCount(1);
			}
		});
		reportUserRightsRepository.saveAll(reportUserRightsModels);
		updateCaching(uniqueCode);
		return Collections.emptyList();
	}

	/**
	 * Updates the cache by removing entries associated with the provided unique code.
	 * 
	 * @param uniqueCode The unique identifier for the cache entries to be removed.
	 */
	private void updateCaching(String uniqueCode) {
		removeEntriesWithUniqueCode(Constants.REPORT_RIGHTS_CACHE, uniqueCode);
	}

	/**
	 * Removes cache entries from the specified cache based on a unique code pattern match.
	 *
	 * @param cacheName The name of the cache to clear entries from.
	 * @param uniqueCode The unique identifier used for cache entry removal.
	 */
	@SuppressWarnings("unchecked")
	private void removeEntriesWithUniqueCode(String cacheName, String uniqueCode) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache.getNativeCache();

			// Use Caffeine's asMap() method to access cache entries and iterate over them
			Set<Object> keysToRemove = nativeCache.asMap().keySet().stream().filter(String.class::isInstance)
				.filter(key -> {
					String[] parts = ((String) key).split("-");
					return parts.length > 1 && parts[1].equalsIgnoreCase(uniqueCode);
				}).collect(Collectors.toSet());

			// Invalidate all matching keys in one go
			nativeCache.invalidateAll(keysToRemove);
		}
	}

}