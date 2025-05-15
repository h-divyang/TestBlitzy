package com.catering.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Configuration class that holds Amazon S3 (Simple Storage Service) related properties.
 * The properties are injected from the application's configuration files (e.g., application.properties or application.yml).
 * 
 * These properties are used for configuring the connection to Amazon S3, including the S3 bucket, region, and access credentials.
 */
@Getter
@Configuration
public class S3Properties {

	@Value("${s3.bucket}")
	private String bucket;

	@Value("${s3.region}")
	private String region;

	@Value("${s3.access.key}")
	private String accessKey;

	@Value("${s3.access.secret}")
	private String accessSecret;

}