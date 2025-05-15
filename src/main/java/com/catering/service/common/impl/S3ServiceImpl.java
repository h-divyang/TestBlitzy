package com.catering.service.common.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.bean.FileBean;
import com.catering.properties.S3Properties;
import com.catering.service.common.S3Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Implementation of the S3Service interface for interacting with Amazon S3.
 * This service provides methods for uploading, retrieving, deleting files,
 * and generating URLs for stored objects in an Amazon S3 bucket.
 * <p>
 * This class leverages S3Client for S3 operations and retrieves configuration
 * properties from S3Properties.
 * <p>
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3ServiceImpl implements S3Service {

	/**
	 * Client for interacting with Amazon S3, enabling storage and retrieval of objects.
	 */
	S3Client s3Client;

	/**
	 * Holds configuration properties for connecting to Amazon S3.
	 */
	S3Properties s3Properties;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(MultipartFile file, String key) throws IOException {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(s3Properties.getBucket())
				.key(key)
				.contentType(file.getContentType())
				.contentLength(file.getSize())
				.acl(ObjectCannedACL.PUBLIC_READ)
				.build();
		s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean get(String key) throws IOException {
		try {
			// Check if object exists using headObject
			HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key(key)
					.build();
			s3Client.headObject(headObjectRequest);

			// Get the object
			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key(key)
					.build();

			ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
			GetObjectResponse objectResponse = response.response();

			return FileBean.builder()
					.file(response.readAllBytes())
					.contentType(objectResponse.contentType())
					.build();
		} catch (NoSuchKeyException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl(String key) throws IOException {
		try {
			// Check if object exists
			HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
					.bucket(s3Properties.getBucket())
					.key(key)
					.build();
			s3Client.headObject(headObjectRequest);

			// Generate URL
			return String.format("https://%s.s3.%s.amazonaws.com/%s?t=%d", s3Properties.getBucket(), s3Client.serviceClientConfiguration().region(), key, System.currentTimeMillis());

		} catch (NoSuchKeyException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createStaticUrl(String key) {
		return String.format("https://%s.s3.%s.amazonaws.com/%s?t=%d", s3Properties.getBucket(), s3Client.serviceClientConfiguration().region(), key, System.currentTimeMillis());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(String key) {
		s3Client.deleteObject(DeleteObjectRequest.builder()
				.bucket(s3Properties.getBucket())
				.key(key)
				.build());
	}

}