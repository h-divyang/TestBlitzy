package com.catering.service.common.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import com.catering.service.common.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the MessageService interface for retrieving and formatting messages based on unique keys.
 * This service uses the Spring MessageSource to fetch messages from resource bundles, supporting internationalization.
 *
 * The keys used for fetching messages should be properly formatted.
 * The service trims outer characters of the provided keys and then resolves them using the configured MessageSource and locale.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImpl implements MessageService {

	/**
	 * Provides access to localized messages and resolves messages based on locale information.
	 */
	MessageSource messageSource;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage(String key) {
		if (StringUtils.isNoneBlank(key)) {
			key = key.substring(1, key.length() - 1);
			return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
		}
		return StringUtils.EMPTY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage(String key, Object ...args) {
		if (StringUtils.isNoneBlank(key)) {
			key = key.substring(1, key.length() - 1);
			return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
		}
		return StringUtils.EMPTY;
	}

}