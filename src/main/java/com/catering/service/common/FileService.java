package com.catering.service.common;

import org.springframework.web.multipart.MultipartFile;

import com.catering.bean.FileBean;

/**
 * Interface providing file-related operations, such as uploading, retrieving, and constructing file paths or URLs.
 */
public interface FileService {

	/**
	 * Uploads a file to the designated storage location using the provided key.
	 *
	 * @param file The file to be uploaded, encapsulated as a MultipartFile.
	 * @param key The unique identifier or path location where the file will be stored.
	 */
	void upload(MultipartFile file, String key);

	/**
	 * Retrieves a file based on the given key.
	 *
	 * @param key The unique identifier or path location of the file to retrieve.
	 * @return A FileBean object containing the file's content and metadata.
	 */
	FileBean get(String key);

	/**
	 * Constructs a URL by combining the given path segments.
	 *
	 * @param paths An array of strings representing the segments to form the URL.
	 * @return A string representing the constructed URL.
	 */
	String getUrl(String... paths);

	/**
	 * Constructs a URL by appending the provided path segments in order.
	 *
	 * @param paths An array of strings representing the segments to be combined to form the URL.
	 * @return A string representing the constructed URL composed of the given path segments.
	 */
	String createUrl(String... paths);

	/**
	 * Constructs a static URL by appending the provided path segments in order.
	 * Mainly used to reduce loading issue to get object from S3 instead create static string path of image
	 *
	 * @param paths An array of strings representing the segments to be combined to form the URL.
	 * @return A string representing the constructed URL same as createUrl function.
	 */
	String createStaticUrl(String... paths);

	/**
	 * Constructs a file path by combining multiple path segments provided as input.
	 *
	 * @param paths An array of strings representing the segments of the file path to be combined.
	 * @return A string representing the constructed file path.
	 */
	String createKey(String... paths);

}