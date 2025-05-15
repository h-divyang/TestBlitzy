package com.catering.util;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.dto.common.FilterDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for handling pagination and sorting operations.
 * <p>This class provides methods to generate page requests, sort requests, 
 * and encapsulate paging information into a custom format.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingUtils {

	/**
	 * Creates a {@link PageRequest} based on the current page and page size parameters.
	 * If the parameters are invalid, it returns an empty {@link Optional}.
	 * 
	 * @param currentPageStr The current page number as a string.
	 * @param sizePerPageStr The size per page as a string.
	 * @return An {@link Optional} containing the {@link PageRequest} if valid parameters, otherwise empty.
	 */
	public static Optional<PageRequest> pageRequestOf(String currentPageStr, String sizePerPageStr) {
		Integer page = 0;
		Integer size = 0;
		if (StringUtils.isNoneBlank(currentPageStr) && StringUtils.isNoneBlank(sizePerPageStr) && ValidationUtils.isNumber(currentPageStr) && ValidationUtils.isNumber(sizePerPageStr)) {
			int pageInt = Integer.parseInt(currentPageStr) - 1;
			int sizeInt = Integer.parseInt(sizePerPageStr);
			if (pageInt >= 0 && sizeInt > 0) {
				page = pageInt;
				size = sizeInt;
			}
		}
		try {
			return Optional.of(PageRequest.of(page, size));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Creates a {@link Sort} object based on the sorting direction and field name.
	 * If the direction or field name is invalid, it returns an unsorted {@link Sort}.
	 * 
	 * @param direction The direction of sorting ("ASC" or "DESC").
	 * @param fieldName The field to sort by.
	 * @param clazz The class of the object to validate the field.
	 * @param <T> The type of the class.
	 * @return An {@link Optional} containing the {@link Sort} if valid parameters, otherwise an unsorted {@link Sort}.
	 */
	public static <T> Optional<Sort> sortOf(String  direction, String fieldName, Class<T> clazz) {
		Optional<Sort> sort = Optional.empty();
		if (StringUtils.isNotBlank(direction) && BeanUtils.isFieldExist(clazz, fieldName)) {
			try {
				sort = Optional.of(Sort.by(Direction.fromString(direction), fieldName));
			} catch (Exception e) {
				sort = Optional.of(Sort.unsorted());
			}
		}
		return sort;
	}

	/**
	 * Creates a {@link Paging} object containing the paging information of the given {@link Page}.
	 * 
	 * @param pages The {@link Page} containing the current page data.
	 * @param <M> The type of the elements in the page.
	 * @return An {@link Optional} containing the {@link Paging} information.
	 */
	public static <M> Optional<Paging> getPaging(Page<M> pages) {
		long lastIndex = pages.getPageable().getOffset() + pages.getSize();
		if (lastIndex > pages.getTotalElements()) {
			lastIndex = pages.getTotalElements();
		}
		return Optional.of(Paging.builder()
			.sizePerPage(pages.getSize())
			.currentPage(pages.getNumber() + 1)
			.totalRecords(pages.getTotalElements())
			.firstIndex(pages.getPageable().getOffset() + 1)
			.lastIndex(lastIndex)
			.totalPages(pages.getTotalPages())
			.totalRecordOfCurrentPage(pages.getNumberOfElements())
			.build());
	}

	/**
	 * Returns the default sorting field if no field is provided.
	 * 
	 * @param sortingField The sorting field name.
	 * @return The provided field or the default sorting field if the provided one is blank.
	 */
	public static String getDefaultSortingField(String sortingField) {
		return StringUtils.isNoneBlank(sortingField) ? sortingField : FieldConstants.COMMON_FIELD_ID;
	}

	/**
	 * Returns the default sorting direction if no direction is provided.
	 * 
	 * @param sortingDirection The sorting direction ("ASC" or "DESC").
	 * @return The provided direction or "DESC" as the default.
	 */
	public static String getDefaultSortingDirection(String sortingDirection) {
		return StringUtils.isNoneBlank(sortingDirection) ? sortingDirection : Direction.DESC.name();
	}

	/**
	 * Builds a {@link Pageable} object based on the pagination information provided in the {@link FilterDto}.
	 * Ensures that the current page and size per page are valid numbers and within a valid range.
	 *
	 * @param <M> the generic type (not directly used here but retained for compatibility)
	 * @param filterDto the DTO containing pagination information
	 * @param class1 the class type of the model (not used in logic, but retained for compatibility)
	 * @return a {@link Pageable} object representing the pagination request
	 */
	public static <M> Pageable buildPageRequestFromFilterDto(FilterDto filterDto) {
		Integer page = 0;
		Integer size = 0;
		if (StringUtils.isNoneBlank(filterDto.getCurrentPage()) && StringUtils.isNoneBlank(filterDto.getSizePerPage()) && ValidationUtils.isNumber(filterDto.getCurrentPage()) && ValidationUtils.isNumber(filterDto.getSizePerPage())) {
			int pageInt = Integer.parseInt(filterDto.getCurrentPage()) - 1;
			int sizeInt = Integer.parseInt(filterDto.getSizePerPage());
			if (pageInt >= 0 && sizeInt > 0) {
				page = pageInt;
				size = sizeInt;
			}
		}
		return (Pageable) PageRequest.of(page, size);
	}

}