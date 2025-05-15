package com.catering.bean;

import com.catering.util.FileUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a file and its associated metadata.
 * <p>
 * This class encapsulates a file as a byte array along with its content type, and provides a method for converting the file 
 * into a binary data URI format for easier transmission or embedding.
 * </p>
 * <p>
 * The class uses Lombok annotations to automatically generate the builder pattern, constructors, and getter methods.
 * </p>
 * 
 * <b>Fields:</b>
 * <ul>
 * <li><b>{@code file}</b>: The byte array representation of the file content.</li>
 * <li><b>{@code contentType}</b>: The MIME type (e.g., "image/png", "application/pdf") of the file.</li>
 * </ul>
 * 
 * <b>Methods:</b>
 * <ul>
 * <li><b>{@code getFileBinary()}</b>: Converts the file content into a data URI format, allowing the file to be transmitted as a string.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileBean {

	/**
	 * The byte array representation of the file content.
	 */
	private byte[] file;

	/**
	 * The MIME type of the file (e.g., "image/png", "application/pdf").
	 */
	private String contentType;

	/**
	 * Converts the file content into a binary data URI format.
	 * This is useful for embedding or transmitting files as part of an HTTP request.
	 * 
	 * @return A string containing the data URI representation of the file.
	 */
	public String getFileBinary() {
		return FileUtils.convertToDataURI(file, contentType);
	}

}