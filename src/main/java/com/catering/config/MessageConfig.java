package com.catering.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.catering.util.DataUtils;

/**
 * Configuration class for setting up internationalization (i18n) message resolution in a Spring application.
 * <p>
 * This configuration class provides the necessary setup to retrieve messages from 
 * properties files (e.g., `messages.properties`, `messages_fr.properties`) based on 
 * the `Accept-Language` header sent in the HTTP request.
 * </p>
 * <p>
 * It uses a custom locale resolver that determines which language-specific message file to load.
 * </p>
 * 
 * @see AcceptHeaderLocaleResolver
 * @see WebMvcConfigurer
 * @see MessageSource
 */
@Configuration
public class MessageConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

	/**
	 * Creates a Bean of MessageSource to retrieve messages from a messages properties file.
	 * <p>
	 * The `messageSource` bean is responsible for loading the message properties from 
	 * the `i18n/messages` file. It supports UTF-8 encoding and message formatting.
	 * </p>
	 * 
	 * @return the configured {@link MessageSource} instance
	 */
	@Bean("messageSource")
	MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		// Specifies the base name of the message properties file
		messageSource.setBasenames("i18n/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		messageSource.setAlwaysUseMessageFormat(true);
		return messageSource;
	}

	/**
	 * Resolves the locale based on the Accept-Language header in the HTTP request.
	 * <p>
	 * If the `Accept-Language` header is missing or empty, the default locale will be used.
	 * Otherwise, it parses the `Accept-Language` header to determine the appropriate locale 
	 * to return. This is used to resolve which `messages.properties` file will be used for 
	 * internationalized responses (e.g., `messages_fr.properties` for French).
	 * </p>
	 * 
	 * @param request the HTTP request object containing the `Accept-Language` header
	 * @return the resolved {@link Locale}
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		// If Accept-Language header is empty, use the system's default locale
		if (StringUtils.isEmpty(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
			return Locale.getDefault();
		}
		// Parse the Accept-Language header and match the best language range
		List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
		return Locale.lookup(list, DataUtils.getLocales());
	}

}