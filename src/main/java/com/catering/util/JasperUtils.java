package com.catering.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catering.constant.Constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for working with Jasper Reports and related resources.
 *
 * <p>This class provides utility methods for:
 * <ul>
 * <li>Creating parameter maps for Jasper Reports with localized values.</li>
 * <li>Building paths for report file locations dynamically.</li>
 * </ul>
 *
 * <p>The class is designed to be non-instantiable.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JasperUtils {

	static Logger logger = LoggerFactory.getLogger(JasperUtils.class);

	/**
	 * Creates a parameter map for a Jasper Report by iterating through the static fields of the given class.
	 * The field names are used as keys, and the field values are used to fetch localized resource strings.
	 *
	 * @param <T> The type of the class containing static fields.
	 * @param locale The {@link Locale} for fetching localized resources.
	 * @param clazz The class containing static fields to be included in the parameter map.
	 * @param key The base key prefix for reading resource values.
	 * @return A map of parameters with field names as keys and localized resource values as values.
	 */
	public static <T> Map<String, Object> createParameterMap(Locale locale, Class<T> clazz, String key) {
		Map<String, Object> parameters = new HashMap<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				try {
					String value = (String) field.get(null);
					parameters.put(field.getName(), ResourceUtils.readResourcesAsString(locale.getLanguage(), key+"."+value));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return parameters;
	}

	/**
	 * Builds a dynamic path for a Jasper Report file using a folder name and a report file name.
	 *
	 * @param dynamicFolderName The folder name (e.g., tenant-specific folder).
	 * @param reportFileName The name of the Jasper Report file.
	 * @return A string representing the dynamic path, with components separated by forward slashes ('/').
	 */
	public static String buildPath(Object dynamicFolderName, String reportFileName) {
		ArrayList<String> pathList = new ArrayList<>();
		pathList.add(Constants.JASPER);
		pathList.add(String.valueOf(dynamicFolderName));
		pathList.add(reportFileName);

		Path path = Path.of(""); // Start with an empty path

		for (String pathComponent : pathList) {
			path = path.resolve(pathComponent);
		}

		return path.toString().replace("\\", "/");
	}

}