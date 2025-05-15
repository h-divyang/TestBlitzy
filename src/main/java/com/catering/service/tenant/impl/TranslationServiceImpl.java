package com.catering.service.tenant.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.catering.dto.tenant.request.TranslateDto;
import com.catering.service.tenant.TranslationService;

/**
 * Service implementation for handling translation operations.
 *
 * This class provides the logic for translating text from a source language
 * into multiple target languages. It uses a third-party API for the translation
 * process and handles the encoding and decoding of text to ensure proper functioning
 * during API interactions.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 */
@Service
public class TranslationServiceImpl implements TranslationService {

	/**
	 * Called translation URL from application.properties file and store in translationUrl variable.
	 */
	@Value("${translation.url}")
	private String translationUrl;

	/**
	 * Access third party API.
	 */
	public RestTemplate restTemplate = new RestTemplate();

	/**
	 * Default constructor for TranslationServiceImpl.
	 * Initializes a new instance of RestTemplate for handling HTTP requests.
	 */
	public TranslationServiceImpl() {
		this.restTemplate = new RestTemplate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] translateText(TranslateDto translateDto) {
		List<Object> results = new ArrayList<>();
		try {
			String encodedText = customEncode(translateDto.getText());
			for (String targetLang : translateDto.getTranslateLanguages()) {
				String url = translationUrl + "?text=" + encodedText + "&ime=transliteration_" + translateDto.getSourceLanguage() + "_" + targetLang;
				ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(url, Object[].class);
				Object[] objects = responseEntity.getBody();
				Object[] decodedObjects = Stream.of(objects)
						.map(object -> customDecode(object.toString())) // URL decode
						.map(object -> htmlDecode(object.toString())) // HTML entity decode
						.toArray(Object[]::new);
				results.add(decodedObjects);
			}
			return results.toArray();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Encodes a given string using UTF-8 encoding and applies custom transformations
	 * for specific characters to align with certain use cases where standard encoding
	 * requires adjustments for special characters.
	 *
	 * @param value The string to encode.
	 * @return The custom encoded string with special character adjustments.
	 */
	private static String customEncode(String value) {
		try {
			// Encode the string
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
				.replace("%2B", "+") // Decode '%2B' back to '+' (plus sign)
				.replace("%7E", "~") // Encode '~' to '%7E'
				.replace("%3B", ";") // Decode '%3B' back to ';' (semicolon)
				.replace("%25", "%") // Decode '%25' back to '%' (percent sign)
				.replace("%5E", "^") // Decode '%5E' back to '^' (caret)
				.replace("%3D", "=") // Decode '%3D' back to '=' (equals sign)
				.replace("%7B", "{") // Decode '%7B' back to '{' (left curly brace)
				.replace("%7D", "}") // Decode '%7D' back to '}' (right curly brace)
				.replace("%3A", ":") // Decode '%3A' back to ':' (colon)
				.replace("%3E", ">") // Decode '%3E' back to '>' (greater-than sign)
				.replace("%3C", "<") // Decode '%3C' back to '<' (less-than sign)
				.replace("%3F", "?") // Decode '%3F' back to '?' (question mark)
				.replace("%2F", "/") // Decode '%2F' back to '/' (forward slash)
				.replace("%5C", "\\") // Decode '%5C' back to '\' (backward slash)
				.replace("%5B", "[") // Decode '%5B' back to '[' (left square bracket)
				.replace("%5D", "]") // Decode '%5D' back to ']' (right square bracket)
				.replace("%0A", "\n"); // Decode '%0A' back to '\n' (new line)
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Decodes a given HTML-encoded string into a normal string by converting
	 * HTML entities back to their corresponding characters.
	 *
	 * @param value The HTML-encoded string to decode.
	 * @return The decoded string where HTML entities have been replaced with their corresponding characters.
	 */
	private static String htmlDecode(String value) {
		return StringEscapeUtils.unescapeHtml4(value);
	}

	/**
	 * Decodes a given UTF-8 encoded string and handles specific exceptions.
	 * This method attempts to decode the provided string using `URLDecoder` with UTF-8 encoding.
	 * If an `UnsupportedEncodingException` occurs, it throws a `RuntimeException`.
	 * If an `IllegalArgumentException` occurs, the original value is returned as-is.
	 *
	 * @param value The UTF-8 encoded string to decode.
	 * @return The decoded string if decoding succeeds, or the original string in case of an illegal argument.
	 */
	private static String customDecode(String value) {
		try {
			// Decode the string
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			return value; // Or handle as necessary
		}
	}

}