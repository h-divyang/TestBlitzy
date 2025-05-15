package com.catering.service.common;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.catering.bean.FileBean;

/**
 * The S3Service interface provides methods for interacting with Amazon S3 storage.
 * It includes functionality for uploading, retrieving, deleting files, and generating URLs for stored objects.
 */
public interface S3Service {

	/**
	 * Uploads a file to a specific storage location with the provided key.
	 *
	 * @param file The file to be uploaded, encapsulated as a MultipartFile.
	 * @param key The unique identifier or path where the file will be stored.
	 * @throws IOException If an I/O error occurs during the file upload process.
	 */
	void upload(MultipartFile file, String key) throws IOException;

	/**
	 * Retrieves a file based on the provided key.
	 *
	 * @param key The unique identifier or path location of the file to retrieve.
	 * @return A FileBean object containing the file's binary data and associated metadata.
	 * @throws IOException If an I/O error occurs during the file retrieval process.
	 */
	FileBean get(String key) throws IOException;

	/**
	 * Generates a URL for the file or object identified by the given key.
	 *
	 * @param key The unique identifier or path of the file or object for which the URL is to be generated.
	 * @return A string representing the generated URL.
	 * @throws IOException If an I/O error occurs during the URL generation process.
	 */
	String getUrl(String key) throws IOException;

	/**
	 * Generates a URL directly without involving S3, It will always return URL if file exist or not.
	 *
	 * @param key The unique identifier or path of the file or object for which the URL is to be generated.
	 * @return A string representing the generated URL.
	 */
	String createStaticUrl(String key);

	/**
	 * Deletes a file or object identified by the given key.
	 *
	 * @param key The unique identifier or path of the file or object to be deleted.
	 */
	void delete(String key);

}