package com.catering.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Configuration class that holds datasource properties for database connectivity.
 * The properties are injected from the application's configuration files (e.g., application.properties or application.yml).
 * 
 * The properties include information such as:
 * - Database connection URL
 * - Username and password for authentication
 * - Driver class name for the JDBC connection
 * - Additional properties (like SSL settings, connection timeout, etc.)
 * - Maximum pool size for connection pooling
 * 
 * This class also provides a method to construct the full database URL by combining the base URL, database name, and additional properties.
 */
@Getter
@Configuration
public class DataSourceProperties {

	@Value("${catering.datasource.name}")
	private String name;

	@Value("${catering.datasource.url}")
	private String url;

	@Value("${catering.datasource.username}")
	private String username;

	@Value("${catering.datasource.password}")
	private String password;

	@Value("${catering.datasource.driverClassName}")
	private String driver;

	@Value("${catering.datasource.properties}")
	private String properties;

	@Value("${catering.datasource.maximum-pool-size}")
	private Integer maximumPoolSize;

	public String getDefaultDataSourceUrl() {
		return url+name+properties;
	}

}