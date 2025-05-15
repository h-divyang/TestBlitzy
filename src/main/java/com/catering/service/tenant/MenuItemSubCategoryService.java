package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.MenuItemSubCategoryDto;
import com.catering.exception.RestException;

/**
 * Service interface for managing MenuItemSubCategory entities and related operations.
 */
public interface MenuItemSubCategoryService {

	/**
	 * Creates or updates a MenuItemSubCategory entity based on the provided DTO.
	 *
	 * @param menuItemSubcategoryDto The data transfer object for the MenuItemSubCategory to be created or updated.
	 * @return An Optional containing the created or updated MenuItemSubCategoryDto, or an empty Optional if the operation fails.
	 * @throws RestException If there is an error during the creation or update process.
	 */
	Optional<MenuItemSubCategoryDto> createAndUpdate(MenuItemSubCategoryDto menuItemSubcategoryDto) throws RestException;

	/**
	 * Retrieves a list of MenuItemSubCategoryDto objects based on the specified filter criteria.
	 *
	 * @param filterDto The filter criteria for querying the MenuItemSubCategory data.
	 * @return A ResponseContainerDto containing a list of MenuItemSubCategoryDto objects and associated metadata.
	 */
	ResponseContainerDto<List<MenuItemSubCategoryDto>> read(FilterDto filterDto);

	/**
	 * Deletes a MenuItemSubCategory entity based on the specified unique identifier.
	 *
	 * @param id The unique identifier of the MenuItemSubCategory to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of all active MenuItemSubCategoryDto objects.
	 *
	 * @return A list of MenuItemSubCategoryDto objects with the 'isActive' status set to true.
	 */
	List<MenuItemSubCategoryDto> findByIsActiveTrue();

}