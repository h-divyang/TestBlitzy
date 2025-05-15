package com.catering.config;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanySettingService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Configuration class for setting up and customizing the Jackson {@link ObjectMapper}.
 * <p>
 * This configuration class defines a Spring bean for the {@link ObjectMapper}, 
 * which is used for serializing and deserializing Java objects to and from JSON.
 * It customizes the {@link ObjectMapper} with additional modules and configuration settings 
 * for handling Java 8 Date/Time API types and ensuring the correct handling of time zones.
 * </p>
 * <p>
 * The configuration includes:
 * <ul>
 *   <li>Disabling the failure on empty beans during serialization.</li>
 *   <li>Registering JavaTimeModule and Jdk8Module to handle Java 8 Date/Time types and Optionals.</li>
 *   <li>Adding custom serializers and deserializers for {@link LocalDateTime} and {@link LocalTime} types.</li>
 *   <li>Configuring the deserialization process to ignore unknown properties in the incoming JSON.</li>
 * </ul>
 * </p>
 * 
 * @see ObjectMapper
 * @see JavaTimeModule
 * @see Jdk8Module
 * @see SimpleModule
 * @see LocalDateTimeSerializer
 * @see LocalTimeSerializer
 * @see LocalDateTimeDeserializer
 * @see LocalTimeDeserializer
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class ObjectMapperConfig {

	/**
	 * Exception handling service for throwing and managing exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service responsible for providing localized messages.
	 */
	MessageService messageService;

	/**
	 * Time zone initializer for managing time zones.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * Service responsible for fetching company-specific settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * Creates and configures an {@link ObjectMapper} bean.
	 * <p>
	 * This method sets up a custom-configured {@link ObjectMapper} with several features:
	 * <ul>
	 *   <li>Disables serialization failure when encountering empty beans.</li>
	 *   <li>Registers {@link JavaTimeModule} and {@link Jdk8Module} to handle Java 8 date/time types and Optionals.</li>
	 *   <li>Registers custom serializers and deserializers for {@link LocalDateTime} and {@link LocalTime}.</li>
	 *   <li>Configures the deserialization process to ignore unknown properties in the input JSON.</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the configured {@link ObjectMapper} instance
	 */
	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeZoneInitializer, companySettingService));
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(exceptionService, messageService));
		module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeZoneInitializer, companySettingService));
		// Register the module to the ObjectMapper
		objectMapper.registerModule(module);
		return objectMapper;
	}

}