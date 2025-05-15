package com.catering.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Configuration class that holds JWT (JSON Web Token) related properties.
 * The properties are injected from the application's configuration files (e.g., application.properties or application.yml).
 * 
 * These properties are used for configuring the JWT mechanism including headers, expiration, authorities, and more.
 */
@Getter
@Configuration
public class JwtProperties {

	@Value("${jwt.header}")
	private String header;

	@Value("${jwt.prefix}")
	private String prefix;

	@Value("${jwt.authorities}")
	private String authorities;

	@Value("${jwt.x-tenant}")
	private String xTenant;

	@Value("${jwt.userId}")
	private String userId;

	@Value("${jwt.expire-time-in-millis}")
	private Long expireTimeInMillis;

	@Value("${jwt.key}")
	private String key;

	@Value("${jwt.uniqueCode}")
	private String uniqueCode;

}