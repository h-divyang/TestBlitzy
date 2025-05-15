package com.catering.util;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for performing operations on arrays, such as merging or building arrays.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayUtils {

	/**
	 * Merges multiple string arrays into a single array.
	 *
	 * @param arrays Varargs of string arrays to be merged.
	 * @return A single array containing all the elements of the input arrays.
	 */
	public static String[] mergeStringArray(String[] ...arrays) {
		return Stream.of(arrays)
			.flatMap(Stream::of)
			.toArray(String[]::new);
	}

	/**
	 * Builds a single string array from a varargs of strings.
	 *
	 * @param arrays Varargs of strings.
	 * @return An array containing the input strings.
	 */
	public static String[] buildArray(String ...arrays) {
		return arrays;
	}

	/**
	 * Merges multiple `Field` arrays into a single array.
	 *
	 * @param arrays Varargs of `Field` arrays to be merged.
	 * @return A single array containing all the `Field` elements of the input arrays.
	 */
	public static Field[] mergeFieldArray(Field[] ...arrays) {
		return Stream.of(arrays)
			.flatMap(Stream::of)
			.toArray(Field[]::new);
	}

}