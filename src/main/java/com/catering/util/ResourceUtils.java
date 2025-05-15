package com.catering.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.catering.constant.PropertiesConstants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for handling resource bundle reading operations.
 * <p>This class provides methods for reading properties from resource bundles and converting them into various formats.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceUtils {

	/**
	 * Retrieves a fixed raw material order string from the resource bundle.
	 * 
	 * @param type The locale type (e.g., "en", "fr").
	 * @return The raw material order string from the resource file, or null if not found.
	 */
	public static String orderGeneralFixRawMaterial(String type) {
		return readResourcesAsString(type, PropertiesConstants.ORDER_GENERAL_FIX_RAW_MATERIAL);
	}

	/**
	 * Retrieves a list of time periods from the resource bundle.
	 * 
	 * @param type The locale type (e.g., "en", "fr").
	 * @return A list of time periods, or an empty list if not found.
	 */
	public static List<String> getTimePeriod(String type) {
		return readResources(type, PropertiesConstants.TIME_PERIOD);
	}

	/**
	 * Reads a comma-separated list of values from the resource bundle for the given key.
	 * 
	 * @param type The locale type (e.g., "en", "fr").
	 * @param key The key in the resource bundle.
	 * @return A list of values corresponding to the key, or an empty list if not found.
	 */
	public static List<String> readResources(String type, String key) {
		if (StringUtils.isNotBlank(key)) {
			ResourceBundle resourceBundle = resourceBundle(type);
			if (Objects.nonNull(resourceBundle)) {
				String value = resourceBundle.getString(key);
				return Arrays.asList(StringUtils.split(value, ','));
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Retrieves the resource bundle for the given locale type.
	 * 
	 * @param type The locale type (e.g., "en", "fr").
	 * @return The resource bundle for the specified locale, or null if not found.
	 */
	private static ResourceBundle resourceBundle(String type) {
		if (StringUtils.isNotBlank(type)) {
			return ResourceBundle.getBundle("data/data", new Locale(type));
		}
		return null;
	}

	/**
	 * Reads a single string value from the resource bundle for the given key.
	 * 
	 * @param type The locale type (e.g., "en", "fr").
	 * @param key The key in the resource bundle.
	 * @return The string value corresponding to the key, or null if not found.
	 */
	public static String readResourcesAsString(String type, String key) {
		if (StringUtils.isNotBlank(key)) {
			ResourceBundle resourceBundle = resourceBundle(type);
			if (Objects.nonNull(resourceBundle)) {
				return resourceBundle.getString(key);
			}
		}
		return null;
	}

}