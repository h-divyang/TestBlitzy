package com.catering.config;

import java.io.IOException;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.catering.interceptor.AuthorizationInterceptor;
import com.catering.properties.JwtProperties;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuHeaderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This configuration class is responsible for customizing Spring MVC's web configuration.
 * It sets up view controllers, static resource handling, interceptors, and formatters.
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * Service for retrieving messages.
	 */
	MessageService messageService;

	/**
	 * Properties for JWT configuration.
	 */
	JwtProperties jwtProperties;

	/**
	 * Service for handling JWT logic.
	 */
	JwtService jwtService;

	/**
	 * Cache manager for caching related operations.
	 */
	CacheManager cacheManager;

	/**
	 * Service for managing the menu header.
	 */
	MenuHeaderService menuHeaderService;

	/**
	 * Converter for handling LocalDateTime conversion.
	 */
	LocalDateTimeConverter localDateTimeConverter;

	/**
	 * Adds view controllers to the Spring MVC configuration.
	 * This method maps the root URL ("/") to a view named "forward:view/index.html".
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:view/index.html");
	}

	/**
	 * Configures resource handlers for static resources.
	 * This method maps resources to be served from the classpath's "static" folder.
	 * If a resource is not found, it falls back to "static/view/index.html".
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").resourceChain(true)
		.addResolver(new PathResourceResolver() {
			@Override
			protected Resource getResource(String resourcePath, Resource location) throws IOException {
				Resource requestedResource = location.createRelative(resourcePath);
				return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
						: new ClassPathResource("/static/view/index.html");
			}
		});
	}

	/**
	 * Adds interceptors for checking API user rights.
	 * This interceptor is added to paths under "/tenant/**".
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthorizationInterceptor(messageService, jwtProperties, jwtService, cacheManager, menuHeaderService)).addPathPatterns("/tenant/**");
	}

	/**
	 * Adds formatters and converters to the Spring MVC configuration.
	 * This method registers a custom converter for LocalDateTime.
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(localDateTimeConverter);
	}

}