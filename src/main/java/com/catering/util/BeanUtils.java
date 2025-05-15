package com.catering.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.catering.constant.FieldConstants;
import com.catering.model.audit.AuditBaseModel;
import com.catering.model.audit.AuditByIdModel;
import com.catering.model.audit.AuditIdModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for operations on Java Beans and their fields.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {

	/**
	 * Checks if a specified field exists in a given class or its audit-related fields.
	 *
	 * @param clazz     The class to check for the field.
	 * @param fieldName The name of the field to check.
	 * @param <T>       The type of the class.
	 * @return True if the field exists; false otherwise.
	 */
	public static <T> boolean isFieldExist(Class<T> clazz, String fieldName) {
		if (StringUtils.isNotBlank(fieldName) && Objects.nonNull(clazz)) {
			String[] fieldNames = StringUtils.split(fieldName, '.');
			if (fieldNames.length == 1) {
				for (Field field : ArrayUtils.mergeFieldArray(getAuditFields(), clazz.getDeclaredFields())) {
					if (field.getName().equalsIgnoreCase(fieldName)) {
						return true;
					}
				}
				return FieldConstants.COMMON_FIELD_ID.equals(fieldName);
			} else {
				Optional<Field> field = getField(clazz, fieldNames[0]);
				if (field.isPresent()) {
					return isFieldExist(field.get().getType(), fieldName.substring(fieldName.indexOf(".") + 1));
				}
			}
		}
		return false;
	}

	/**
	 * Retrieves a field by name from a given class or its audit-related fields.
	 *
	 * @param clazz     The class to retrieve the field from.
	 * @param fieldName The name of the field.
	 * @param <T>       The type of the class.
	 * @return An Optional containing the Field if found; otherwise empty.
	 */
	public static <T> Optional<Field> getField(Class<T> clazz, String fieldName) {
		Optional<Field> field = Optional.empty();
		if (StringUtils.isNotBlank(fieldName) && Objects.nonNull(clazz)) {
			try {
				field = Optional.of(clazz.getDeclaredField(fieldName));
			} catch (NoSuchFieldException | SecurityException e) {
				for (Field f : getAuditFields()) {
					if (f.getName().equalsIgnoreCase(fieldName) || FieldConstants.COMMON_FIELD_ID.equals(fieldName)) {
						field = Optional.of(f);
					}
				}
			}
		}
		return field;
	}

	/**
	 * Concatenates audit field names with a given prefix.
	 *
	 * @param fieldName The prefix to concatenate with the audit field names.
	 * @return An array of concatenated audit field names.
	 */
	public static String[] getAuditFieldsNameWithConcat(String fieldName) {
		return Arrays.asList(getAuditFields()).stream().map(field -> fieldName + "." + field.getName()).toArray(String[]::new);
	}

	/**
	 * Concatenates audit field names with a given prefix, excluding the "id" field.
	 *
	 * @param fieldName The prefix to concatenate with the audit field names.
	 * @return An array of concatenated audit field names without the "id" field.
	 */
	public static String[] getAuditFieldsNameWithConcatWithoutId(String fieldName) {
		return Arrays.asList(getAuditFields()).stream().filter(field -> !field.getName().equals("id")).map(field -> fieldName + "." + field.getName()).toArray(String[]::new);
	}

	/**
	 * Retrieves all audit-related fields.
	 *
	 * @return An array of audit-related fields.
	 */
	private static Field[] getAuditFields() {
		return Arrays.asList(ArrayUtils.mergeFieldArray(AuditIdModel.class.getDeclaredFields(), AuditByIdModel.class.getDeclaredFields(), AuditBaseModel.class.getDeclaredFields())).stream().toArray(Field[]::new);
	}

	/**
	 * Retrieves the names of all audit-related fields.
	 *
	 * @return An array of audit field names.
	 */
	public static String[] getAuditFieldsName() {
		return Arrays.asList(getAuditFields()).stream().map(Field::getName).toArray(String[]::new);
	}

	/**
	 * Retrieves the names of all audit-related fields, excluding the "id" field.
	 *
	 * @return An array of audit field names without the "id" field.
	 */
	public static String[] getAuditFieldsNameWithoutId() {
		List<String> fields = Arrays.asList(getAuditFields()).stream().map(Field::getName).toList();
		return fields.stream().filter(field -> !field.equals("id")).toArray(String[]::new);
	}

}