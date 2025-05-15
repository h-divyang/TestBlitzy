package com.catering.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.catering.properties.S3Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Configuration class for setting up an Amazon S3 client.
 * <p>
 * This configuration class defines a Spring bean for the {@link S3Client}
 * which is used to interact with Amazon Simple Storage Service (S3) in the AWS cloud.
 * The class uses the AWS SDK to authenticate with S3 using the provided credentials
 * (access key and secret key) and region settings from {@link S3Properties}.
 * </p>
 * <p>
 * The configuration includes:
 * <ul>
 *   <li>Creating an Amazon S3 client instance using the provided credentials and region.</li>
 *   <li>Providing the client to be used for interacting with AWS S3 services.</li>
 * </ul>
 * </p>
 *
 * @see S3Client
 * @see AwsBasicCredentials
 * @see StaticCredentialsProvider
 * @see Region
 */
@Configuration
public class S3Config {

	/**
	 * Creates and configures an {@link S3Client} bean.
	 * <p>
	 * This method constructs an {@link S3Client} using the provided AWS access
	 * key and secret key (from {@link S3Properties}). The client is configured with
	 * a specific AWS region, which is also sourced from {@link S3Properties}.
	 * </p>
	 *
	 * @param s3Properties The S3 properties containing AWS credentials and region information
	 * @return the configured {@link S3Client} instance
	 */
	@Bean
	S3Client s3Client(S3Properties s3Properties) {
		// Create AWS credentials using provided access key and secret key
		AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getAccessSecret());
		return S3Client.builder()
				.credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
				.region(Region.of(s3Properties.getRegion()))
				.build();
	}

}