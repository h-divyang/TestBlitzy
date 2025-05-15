package com.catering.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Configuration class that holds file-related properties for managing file uploads and storage paths.
 * The properties are injected from the application's configuration files (e.g., application.properties or application.yml).
 * 
 * The properties include:
 * - Maximum allowed size for image files
 * - Path for file storage in the production environment
 * - Path for file storage in the development environment
 */
@Getter
@Configuration
public class FileProperties {

	@Value("${file.image-max-size}")
	private long imageMaxSize;

	@Value("${file.production-path}")
	private String productionPath;

	@Value("${file.dev-path}")
	private String devPath;

}