package com.catering.service.common.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.bean.FileBean;
import com.catering.constant.Constants;
import com.catering.properties.FileProperties;
import com.catering.properties.ServerProperties;
import com.catering.service.common.FileService;
import com.catering.service.common.S3Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link FileService} interface that provides file-related operations,
 * such as uploading files, retrieving file metadata, constructing file paths based on context,
 * and generating URLs for files stored in Amazon S3. This class integrates with the {@link S3Service}
 * and utilizes server and file-related configurations for customized behavior.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements FileService {

	Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/**
	 * Handles operations related to Amazon S3 storage services.
	 */
	S3Service s3Service;

	/**
	 * Contains configuration properties for the server.
	 */
	ServerProperties serverProperties;

	/**
	 * Stores configuration properties for file handling.
	 */
	FileProperties fileProperties;

	/**
	 * Represents the HTTP request received from the client.
	 */
	HttpServletRequest httpServletRequest;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(MultipartFile file, String key) {
		try {
			s3Service.upload(file, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean get(String key) {
		try {
			return s3Service.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl(String... paths) {
		try {
			Object tenant = httpServletRequest.getAttribute(Constants.TENANT);
			return s3Service.getUrl(getCommonFilePath(Objects.nonNull(tenant) ? tenant.toString() : null, paths));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createUrl(String... paths) {
		try {
			return s3Service.getUrl(getCommonFilePath(null, paths));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createStaticUrl(String... paths) {
		Object tenant = httpServletRequest.getAttribute(Constants.TENANT);
		return s3Service.createStaticUrl(getCommonFilePath(Objects.nonNull(tenant) ? tenant.toString() : null, paths));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createKey(String... paths) {
		Object tenant = httpServletRequest.getAttribute(Constants.TENANT);
		return getCommonFilePath(Objects.nonNull(tenant) ? tenant.toString() : null, paths);
	}

	/**
	 * Constructs a common file path by joining the provided tenant and additional path segments
	 * with the appropriate base directory according to the application's environment.
	 *
	 * @param tenant The tenant identifier which will be used as the base segment of the path; if null, an empty string is returned.
	 * @param paths Additional path segments to append to the tenant path.
	 * @return The constructed file path, normalized and with forward slashes.
	 */
	private String getCommonFilePath(String tenant, String... paths) {
		List<String> pathList = new ArrayList<>(Arrays.asList(paths));
		if (StringUtils.isNotBlank(tenant)) {
			pathList.add(0, tenant);
		}
		Path path = Path.of(serverProperties.isProduction() ? fileProperties.getProductionPath() : fileProperties.getDevPath(), pathList.stream().toArray(String[]::new));
		return path.toString().replace("\\", "/");
	}

}