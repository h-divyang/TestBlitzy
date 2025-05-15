package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class CommonRawMaterialDto {

	private String reportName;

	private Long orderId;

	private Long functionId;

	private Boolean isExtra;

	private String functionName;

	private String rawMaterialCategory;

	private String rawMaterial;

	private Double finalQty;

	private Long finalMeasurementId;

	private String menuItemAndRawMaterialId;

	private Integer decimalLimitQty;

	private Boolean isAllFuntions;

	private Long contactId;

	private String contactName;

	private String venue;

	private Long supplierId;

	private String supplierName;

	private String orderDateRef;

	private Long contactAgencyId;

	private String agency;

	private String orderTimeRef;

	private String timePeriod;

	private String menuItemGroup;

	// List of mandatory fields that must always be included
	private static final List<String> MANDATORY_FIELDS = List.of("orderId", "venue", "functionId", "isExtra", "rawMaterialCategory", "rawMaterial", "finalQty", "finalMeasurementId", "menuItemAndRawMaterialId", "decimalLimitQty", "isAllFuntions");

	/**
	 * Converts a list of DTOs into CommonRawMaterialDto objects.
	 *
	 * @param dtos           List of source DTO objects
	 * @param reportName     The report name to set in each CommonRawMaterialDto
	 * @param optionalFields Additional optional fields to include in the conversion
	 * @return List of converted CommonRawMaterialDto objects
	 */
	public static <T> List<CommonRawMaterialDto> convert(List<T> dtos, String reportName, List<String> optionalFields) {
		// Combine mandatory and optional fields, ensuring uniqueness
		List<String> allFields = Stream.concat(MANDATORY_FIELDS.stream(), optionalFields.stream()).distinct().toList();

		// Cache setter methods to avoid repeated reflection lookups
		Map<String, Method> setterMethods = Arrays.stream(CommonRawMaterialDto.class.getMethods())
				.filter(m -> m.getName().startsWith("set") && m.getParameterCount() == 1)
				.collect(Collectors.toMap(m -> m.getName().substring(3).toLowerCase(), m -> m));

		// Convert each DTO object into a CommonRawMaterialDto
		return dtos.stream().map(dto -> mapToCommonDto(dto, reportName, allFields, setterMethods)).toList();
	}

	/**
	 * Maps a single DTO object to a CommonRawMaterialDto.
	 *
	 * @param dto The source DTO object
	 * @param reportName Report name to set in the new CommonRawMaterialDto
	 * @param fields Fields to be copied from the source DTO
	 * @param setters Cached setter methods for CommonRawMaterialDto
	 * @return A new CommonRawMaterialDto with values copied from the source DTO
	 */
	private static <T> CommonRawMaterialDto mapToCommonDto(T dto, String reportName, List<String> fields, Map<String, Method> setters) {
		CommonRawMaterialDto commonRawMaterialDto = new CommonRawMaterialDto();
		commonRawMaterialDto.setReportName(reportName);
		for (String field : fields) {
			String capitalizedField = capitalize(field);
			Object value = invokeGetter(dto, "get" + capitalizedField);
			if (value != null) {
				Method setter = setters.get(capitalizedField.toLowerCase());
				if (setter != null) {
					try {
						setter.invoke(commonRawMaterialDto, value);
					} catch (Exception ignored) {
						return null;
					}
				}
			}
		}
		return commonRawMaterialDto;
	}

	/**
	 * Invokes the getter method for a given field.
	 *
	 * @param dto The source object
	 * @param methodName The getter method name to invoke
	 * @return The value returned by the getter method, or null if the method does not exist
	 */
	private static <T> Object invokeGetter(T dto, String methodName) {
		try {
			return dto.getClass().getMethod(methodName).invoke(dto);
		} catch (Exception ignored) {
			return null;
		} 
	}

	/**
	 * Capitalizes the first letter of a given string.
	 *
	 * @param str Input string
	 * @return String with the first letter capitalized
	 */
	private static String capitalize(String str) {
		return str.isEmpty() ? str : Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

}