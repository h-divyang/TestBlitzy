package com.catering.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.MenuWithUserRightsDto;
import com.catering.properties.JwtProperties;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuHeaderService;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Interceptor class responsible for handling authorization checks based on user rights.<br>
 * This interceptor is used to ensure that only users with the required rights can access specific methods.
 *
 * @author Krushali Talaviya
 * @since 2024-05-07
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizationInterceptor implements HandlerInterceptor {

	/**
	 * The service responsible for handling messages and translations.
	 * It is typically used to retrieve and manage application-specific messages.
	 */
	MessageService messageService;

	/**
	 * Configuration properties related to JWT (JSON Web Token) authentication.
	 * Contains settings such as JWT header, prefix, expiration time, and the secret key used for signing and verifying JWTs.
	 */
	JwtProperties jwtProperties;

	/**
	 * Service for handling JWT-related operations, such as generating and validating tokens.
	 * It typically uses the settings from {@link JwtProperties} to work with JWTs.
	 */
	JwtService jwtService;

	/**
	 * The cache manager responsible for managing the application-wide cache.
	 * It is used to interact with the cache, such as adding, retrieving, and invalidating cache entries.
	 */
	CacheManager cacheManager;

	/**
	 * The service that manages the menu headers within the application.
	 * It is responsible for retrieving, updating, and managing the navigation menus and their associated headers.
	 */
	MenuHeaderService menuHeaderService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod handlerMethod && (handlerMethod.hasMethodAnnotation(AuthorizeUserRights.class))) {
			String uniqueCode = jwtService.extractUniqueCode(request.getHeader(jwtProperties.getHeader()).substring(7));
			String id = jwtService.extractUserId(request.getHeader(jwtProperties.getHeader()).substring(7)).toString();
			List<MenuWithUserRightsDto> listOfMenu = getCachedData(id, uniqueCode);
			Set<String> staticUserRights = getCachedUserRights(id, uniqueCode);

			if (staticUserRights == null) {
				// User rights not cached, calculate them and store in cache
				staticUserRights = Objects.nonNull(listOfMenu) ? getListOfView(listOfMenu) : Collections.emptySet();
				cacheUserRights(id, uniqueCode, staticUserRights);
			}
			AuthorizeUserRights authorizeUserRights = handlerMethod.getMethodAnnotation(AuthorizeUserRights.class);
			String[] requiredRights = Objects.nonNull(authorizeUserRights) ? authorizeUserRights.value() : null;
			boolean checkAll = Objects.isNull(authorizeUserRights) || authorizeUserRights.checkAll(); // Default to true if not specified

			// Perform authorization check
			if (requiredRights == null || requiredRights.length == 0 || (checkAll && !hasAllRequiredRights(requiredRights, staticUserRights)) || (!checkAll && !hasOneRequiredRight(requiredRights, staticUserRights))) {
				response.setStatus(HttpStatus.OK.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding(StandardCharsets.UTF_8.name());
				response.getWriter().write(new JSONObject(RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.ACCESS_DENIED), HttpStatus.OK)).toString());
				return false;
			}

		}
		return true;
	}

	/**
	 * Retrieves the list of user rights based on the provided list of menus with user rights.
	 *
	 * @param listOfMenus The list of menus with user rights.
	 * @return A set of user rights.
	 */
	private Set<String> getListOfView(List<MenuWithUserRightsDto> listOfMenus) {
		Set<String> staticUserRights = new HashSet<>();
		listOfMenus.forEach(menu -> {
			if (!menu.isGroupTitle() && !menu.getSubMenu().isEmpty()) {
				menu.getSubMenu().forEach(subMenu -> addUserRights(subMenu, staticUserRights));
			} else if (!menu.isGroupTitle() && menu.getSubMenu().isEmpty()) {
				addUserRights(menu, staticUserRights);
			}
		});
		return staticUserRights;
	}

	/**
	 * Adds user rights to the provided set based on the menu's permissions.
	 *
	 * @param menuWithUserRightsDto The menu with user rights.
	 * @param userRightsSet         The set to which user rights are added.
	 */
	private void addUserRights(MenuWithUserRightsDto menuWithUserRightsDto, Set<String> userRightsSet) {
		if (Boolean.TRUE.equals(menuWithUserRightsDto.getCanAdd())) {
			userRightsSet.add(menuWithUserRightsDto.getApiRightsName() + ApiUserRightsConstants.CAN_ADD);
		}
		if (Boolean.TRUE.equals(menuWithUserRightsDto.getCanEdit())) {
			userRightsSet.add(menuWithUserRightsDto.getApiRightsName() + ApiUserRightsConstants.CAN_EDIT);
		}
		if (Boolean.TRUE.equals(menuWithUserRightsDto.getCanDelete())) {
			userRightsSet.add(menuWithUserRightsDto.getApiRightsName() + ApiUserRightsConstants.CAN_DELETE);
		}
		if (Boolean.TRUE.equals(menuWithUserRightsDto.getCanView())) {
			userRightsSet.add(menuWithUserRightsDto.getApiRightsName() + ApiUserRightsConstants.CAN_VIEW);
		}
		if (Boolean.TRUE.equals(menuWithUserRightsDto.getCanPrint())) {
			userRightsSet.add(menuWithUserRightsDto.getApiRightsName() + ApiUserRightsConstants.CAN_PRINT);
		}
	}

	/**
	 * Caches the user rights for a specific user.
	 *
	 * @param id         The user ID.
	 * @param uniqueCode The unique code associated with the user.
	 * @param userRights The set of user rights to be cached.
	 */
	private void cacheUserRights(String id, String uniqueCode, Set<String> userRights) {
		String cacheKey = id + "-" + uniqueCode;
		Cache cache = cacheManager.getCache(Constants.USER_RIGHTS_CACHE);
		if (Objects.isNull(cache)) {
			 cacheManager.getCacheNames().add(Constants.USER_RIGHTS_CACHE);
		} else {
			cache.put(cacheKey, userRights);
		}
	}

	/**
	 * Retrieves the cached user rights for a specific user.
	 *
	 * @param id         The user ID.
	 * @param uniqueCode The unique code associated with the user.
	 * @return The set of cached user rights.
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getCachedUserRights(String id, String uniqueCode) {
		String cacheKey = id + "-" + uniqueCode;
		Cache cache = cacheManager.getCache(Constants.USER_RIGHTS_CACHE);
		if (Objects.nonNull(cache)) {
			return cache.get(cacheKey, Set.class);
		} else {
			return Collections.emptySet();
		}
	}

	/**
	 * Retrieves the cached data from the cache for the given user ID and unique code.
	 *
	 * @param id         The user ID.
	 * @param uniqueCode The unique code associated with the user.
	 * @return The cached data.
	 */
	@SuppressWarnings("unchecked")
	private List<MenuWithUserRightsDto> getCachedData(String id, String uniqueCode) {
		String cacheKey = id + "-" + uniqueCode;
		Cache cache = cacheManager.getCache(Constants.SIDEBAR_ALL_MENUS_CACHE);
		if (Objects.nonNull(cache) && Objects.nonNull(cache.get(cacheKey) )) {
			return (List<MenuWithUserRightsDto>) cache.get(cacheKey).get() ;
		} else {
			// Data not found in cache, retrieve from service and store in cache
			List<MenuWithUserRightsDto> data = menuHeaderService.getAllSidebarItems(Long.parseLong(id), uniqueCode);
			if (Objects.nonNull(data) && Objects.isNull(cache)) {
				cacheManager.getCacheNames().add(Constants.SIDEBAR_ALL_MENUS_CACHE);
			} else if (Objects.nonNull(data)) {
				cache.put(cacheKey, data);
			}
			return data;
		}
	}

	/**
	 * Checks if the user has all the required rights.
	 *
	 * @param requiredRights  The array of required user rights.
	 * @param staticUserRights The set of user rights possessed by the user.
	 * @return True if the user has all the required rights, otherwise false.
	 */
	private boolean hasAllRequiredRights(String[] requiredRights, Set<String> staticUserRights) {
		// Check if the user has all required rights
		return Arrays.stream(requiredRights).allMatch(staticUserRights::contains);
	}

	/**
	 * Checks if the user has at least one of the required rights.
	 *
	 * @param requiredRights  The array of required user rights.
	 * @param staticUserRights The set of user rights possessed by the user.
	 * @return True if the user has at least one of the required rights, otherwise false.
	 */
	private boolean hasOneRequiredRight(String[] requiredRights, Set<String> staticUserRights) {
		// Check if the user has at least one of the required rights
		return Arrays.stream(requiredRights).anyMatch(staticUserRights::contains);
	}

}