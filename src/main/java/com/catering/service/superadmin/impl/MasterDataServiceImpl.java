package com.catering.service.superadmin.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.catering.constant.ApiPathConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.superadmin.LanguageDto;
import com.catering.dto.superadmin.SubscriptionResponseDto;
import com.catering.properties.ServerProperties;
import com.catering.service.superadmin.MasterDataService;
import com.catering.util.HttpClientUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of {@link MasterDataService} for retrieving master data such as language lists and subscription details.
 *
 * This class interacts with external APIs via HTTP requests to fetch master data and uses an ObjectMapper to convert the response data
 * into specific DTO objects. It relies on server configuration properties to determine the appropriate API endpoint.
 *
 * The class is annotated with {@link Service} to indicate its role in the Spring framework, {@link RequiredArgsConstructor} to
 * automatically generate a constructor with required dependencies, and {@link FieldDefaults} to set default field access levels.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MasterDataServiceImpl implements MasterDataService {

	Logger logger = LoggerFactory.getLogger(MasterDataServiceImpl.class);

	/**
	 * Configuration properties specific to the server.
	 */
	ServerProperties serverProperties;

	/**
	 * Utility for converting Java objects to and from JSON.
	 */
	ObjectMapper objectMapper;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LanguageDto> getLanguageList() throws IOException, InterruptedException {
		ResponseContainerDto<List<Map<String, Object>>> languageList = HttpClientUtils.get(serverProperties.getRootUrl() + ApiPathConstant.LANGUAGE, ResponseContainerDto.class, objectMapper);
		if (Objects.nonNull(languageList)) {
			return convert(LanguageDto.class, languageList.getBody());
		}
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionResponseDto> getSubscriptionList() {
		ResponseContainerDto<List<Map<String, Object>>> subscriptionList;
		try {
			subscriptionList = HttpClientUtils.get(serverProperties.getAdminRootUrl() + ApiPathConstant.SUBSCRIPTIONS, ResponseContainerDto.class, objectMapper);
			if (Objects.nonNull(subscriptionList)) {
				return convert(SubscriptionResponseDto.class, subscriptionList.getBody());
			}
		} catch (InterruptedException | IOException e) {
			logger.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
		return Collections.emptyList();
	}

	/**
	 * Converts a list of maps into a list of objects of the specified type.
	 *
	 * @param <T> The type of objects to be converted to.
	 * @param type The class type to which the map should be converted.
	 * @param mapList A list of maps where each map represents the data for an object.
	 * @return A list of objects of the specified type, or an empty list if the input list is null.
	 */
	private <T> List<T> convert(Class<T> type, List<Map<String, Object>> mapList) {
		if (Objects.nonNull(mapList)) {
			List<T> list = new ArrayList<>();
			mapList.forEach(map -> list.add(objectMapper.convertValue(map, type)));
			return list;
		}
		return Collections.emptyList();
	}

}