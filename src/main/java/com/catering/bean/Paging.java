package com.catering.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents pagination details used for paginated responses.
 * <p>
 * This class contains the necessary fields to represent a paginated data set, including the current page, size per page,
 * the first and last index of the current page, total records, total pages, and the total records of the current page.
 * It is typically used to manage pagination in APIs that return large datasets.
 * </p>
 * <p>
 * The class uses Lombok annotations to automatically generate getter methods, constructors, and the builder pattern.
 * </p>
 * 
 * <b>Fields:</b>
 * <ul>
 * <li><b>{@code currentPage}</b>: The current page number, starting from 0. Default is 0.</li>
 * <li><b>{@code sizePerPage}</b>: The number of records per page. Default is 0.</li>
 * <li><b>{@code firstIndex}</b>: The first index of the current page. Default is 0.</li>
 * <li><b>{@code lastIndex}</b>: The last index of the current page. Default is 0.</li>
 * <li><b>{@code totalRecords}</b>: The total number of records available. Default is 0.</li>
 * <li><b>{@code totalPages}</b>: The total number of pages available. Default is 0.</li>
 * <li><b>{@code totalRecordOfCurrentPage}</b>: The total number of records in the current page. Default is 0.</li>
 * </ul>
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Paging {

	/**
	 * The current page number (starting from 0).
	 */
	@Builder.Default
	private int currentPage = 0;

	/**
	 * The number of records per page.
	 */
	@Builder.Default
	private int sizePerPage = 0;

	/**
	 * The first index of the current page.
	 */
	@Builder.Default
	private long firstIndex = 0;

	/**
	 * The last index of the current page.
	 */
	@Builder.Default
	private long lastIndex = 0;

	/**
	 * The total number of records.
	 */
	@Builder.Default
	private long totalRecords = 0;

	/**
	 * The total number of pages.
	 */
	@Builder.Default
	private long totalPages = 0;

	/**
	 * The total number of records on the current page.
	 */
	@Builder.Default
	private long totalRecordOfCurrentPage = 0;

}