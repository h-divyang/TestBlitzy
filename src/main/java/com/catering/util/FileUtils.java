package com.catering.util;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import org.springframework.cache.Cache;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.catering.bean.ErrorGenerator;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.service.common.FileService;
import com.catering.service.common.MessageService;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for file-related operations such as image validation, 
 * size checking, data URI conversion, and cache handling for file storage.
 *
 * <p>This class provides static methods for working with files (e.g., images)
 * in various contexts, including validation against size limits and supported types.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * boolean isValidImage = FileUtils.isImage(file);
 * ErrorGenerator errors = FileUtils.imageValidate(file, 1048576, messageService); // 1 MB size limit
 * }</pre>
 *
 * <p>This class is designed to be non-instantiable.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	/**
	 * Checks if the given file is an image of type JPEG or PNG.
	 *
	 * @param file The {@link MultipartFile} to check.
	 * @return {@code true} if the file is a valid image; {@code false} otherwise.
	 */
	public static boolean isImage(MultipartFile file) {
		return Objects.nonNull(file) && Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE).contains(file.getContentType());
	}

	/**
	 * Checks if the given image file is within the specified size limit.
	 *
	 * @param file The {@link MultipartFile} to check.
	 * @param size The maximum allowed size in bytes.
	 * @return {@code true} if the file size is within the limit; {@code false} otherwise.
	 */
	public static boolean isImageWithinSizeLimit(MultipartFile file, long size) {
		return Objects.nonNull(file) && file.getSize() <= size;
	}

	/**
	 * Converts binary data to a Base64-encoded data URI.
	 *
	 * @param data The binary data to encode.
	 * @param contentType The content type of the file (e.g., "image/png").
	 * @return A Base64-encoded data URI as a {@code String}.
	 */
	public static String convertToDataURI(byte[] data, String contentType) {
		String base64Data = Base64.getEncoder().encodeToString(data);
		return "data:" + contentType + ";base64," + base64Data;
	}

	/**
	 * Validates the given image file for type and size constraints.
	 *
	 * @param file The {@link MultipartFile} to validate.
	 * @param size The maximum allowed size in bytes.
	 * @param messageService The {@link MessageService} for retrieving localized messages.
	 * @return An {@link ErrorGenerator} containing validation errors, if any.
	 */
	public static ErrorGenerator imageValidate(MultipartFile file, long size, MessageService messageService) {
		ErrorGenerator errors = ErrorGenerator.builder();
		if (Objects.nonNull(file) && !FileUtils.isImage(file)) {
			errors.putError(Constants.IMAGE, messageService.getMessage(MessagesConstant.FILE_UNSUPPORTED_IMAGE_TYPE));
		}
		if (Objects.nonNull(file) && !FileUtils.isImageWithinSizeLimit(file, size)) {
			errors.putError(Constants.IMAGE, messageService.getMessage(MessagesConstant.FILE_IMAGE_SIZE_EXCEEDED, Math.round((double)size / (1024 * 1024))));
		}
		return errors;
	}

	/**
	 * Retrieves the company logo and caches it for the given tenant.
	 *
	 * @param fileService The {@link FileService} for file retrieval.
	 * @param cache The {@link Cache} for storing the logo.
	 * @param tenant The tenant identifier used to locate the logo.
	 * @return URL of image
	 */
	public static String getCompanyLogoAndSetInCache(FileService fileService, Cache cache, Object tenant) {
		String logo = fileService.getUrl(Constants.LOGO);
		cache.put(tenant.toString() + "-" + Constants.LOGO, logo);
		return logo;
	}

}