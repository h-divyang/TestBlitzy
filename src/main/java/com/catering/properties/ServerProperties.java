package com.catering.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.Getter;

/**
 * Configuration class that holds server-related properties.
 * Extends {@link org.springframework.boot.autoconfigure.web.ServerProperties} to customize server settings.
 * 
 * <p>The class contains properties related to both the backend and frontend server URLs, including separate URLs for production and local environments.
 * It also provides methods to retrieve the appropriate root and frontend URLs based on the current environment (production or local).</p>
 */
@Primary
@Configuration
public class ServerProperties extends org.springframework.boot.autoconfigure.web.ServerProperties {

	@Getter
	@Value("${server.frontend.port}")
	private String frontendPort;

	@Getter
	@Value("${server.production}")
	private boolean isProduction;

	@Value("${server.local.root-url}")
	private String localUrl;

	@Value("${server.prod.url}")
	private String productionUrl;

	@Value("${server.local.frontend-url}")
	private String frontendLocalUrl;

	@Value("${server.prod.frontend.url}")
	private String frontendProductionUrl;

	@Value("${server.admin.local.root-url}")
	private String adminLocalUrl;

	@Value("${server.admin.prod.url}")
	private String adminProductionUrl;

	@Value("${server.admin.local.frontend-url}")
	private String adminFrontendLocalUrl;

	@Value("${server.admin.prod.frontend-url}")
	private String adminFrontendProductionUrl;

	/**
	 * Returns the root URL based on the current environment.
	 * 
	 * @return the root URL for the current environment (either production or local).
	 */
	public String getRootUrl() {
		return isProduction ? productionUrl : localUrl;
	}

	/**
	 * Returns the frontend URL based on the current environment.
	 * 
	 * @return the frontend URL for the current environment (either production or local).
	 */
	public String getFrontendUrl() {
		return isProduction ? frontendProductionUrl : frontendLocalUrl;
	}

	/**
	 * Returns the admin root URL based on the current environment.
	 * 
	 * @return the admin root URL for the current environment (either production or local).
	 */
	public String getAdminRootUrl() {
		return isProduction ? adminProductionUrl : adminLocalUrl;
	}

	/**
	 * Returns the admin frontend URL based on the current environment.
	 * 
	 * @return the admin frontend URL for the current environment (either production or local).
	 */
	public String getAdminFrontendUrl() {
		return isProduction ? adminFrontendProductionUrl : adminFrontendLocalUrl;
	}

}