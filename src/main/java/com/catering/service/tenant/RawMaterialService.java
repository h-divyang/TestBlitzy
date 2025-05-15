package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.SearchFieldDto;
import com.catering.dto.tenant.request.AllRawMaterialSupplierDto;
import com.catering.dto.tenant.request.NameDto;
import com.catering.dto.tenant.request.RawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialModel;
import com.catering.service.common.GenericService;

/**
 * Interface providing service operations for managing raw materials.
 * This includes CRUD operations, searching, validation, and specific queries related to raw materials.
 * Extends the generic service interface for additional functionality.
 */
public interface RawMaterialService extends GenericService<RawMaterialDto, RawMaterialModel, Long> {

	/**
	 * Creates a new RawMaterial entry if it does not exist or updates an existing RawMaterial entry based on the provided data.
	 *
	 * @param rawMaterialDto The data transfer object containing information about the raw material to be created or updated.
	 * @return An Optional containing the created or updated RawMaterialDto object, or an empty Optional if the operation fails.
	 * @throws RestException If an error occurs during the creation or update process.
	 */
	Optional<RawMaterialDto> createAndUpdate(RawMaterialDto rawMaterialDto) throws RestException;

	/**
	 * Retrieves a list of raw materials based on the provided filter and search criteria.
	 *
	 * @param filterDto The filter parameters such as pagination and sorting details.
	 * @param searchFieldDto The search criteria specifying the field name and value to filter the results.
	 * @return A ResponseContainerDto object containing a list of RawMaterialDto, along with metadata such as status and paging information.
	 */
	ResponseContainerDto<List<RawMaterialDto>> read(FilterDto filterDto, SearchFieldDto searchFieldDto);

	/**
	 * Deletes a raw material record based on its unique identifier.
	 *
	 * @param id The unique identifier of the raw material to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Checks if an entity exists with the given ID. If the entity does not exist, a {@code RestException} is thrown.
	 *
	 * @param id The unique identifier of the entity to be checked.
	 * @throws RestException If the entity with the given ID does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Retrieves a unique list of raw material response DTOs based on the provided filters and identifiers.
	 *
	 * @param dtoType The class type of the DTO to transform the raw material data into.
	 * @param modelType The class type of the raw material model to map from.
	 * @param IDs The list of unique IDs to filter the raw materials.
	 * @param finalMaterialId The identifier for the final material to include in the transformation.
	 * @param userEditId The user identifier used for filtering or context-based operations.
	 * @return A response container object containing a list of {@code RawMaterialResponseDto}, including metadata such as status and paging information.
	 */
	ResponseContainerDto<List<RawMaterialResponseDto>> uniqueList(Class<RawMaterialResponseDto> dtoType, Class<RawMaterialModel> modelType, List<Long> IDs, Long finalMaterialId, Long userEditId);

	/**
	 * Retrieves a list of NameDto objects associated with the specified menu item ID.
	 *
	 * @param menuItemId The unique identifier of the menu item for which NameDto objects are to be retrieved.
	 * @return A list of NameDto objects containing information such as names in different languages,
	 *		   pricing, and associated package menu item categories. The list may be empty if no data is found.
	 */
	List<NameDto> findByMenuItemId(Long menuItemId);

	/**
	 * Retrieves a list of all available raw material data transfer objects (DTOs).
	 *
	 * @return A list of RawMaterialDto containing data about the raw materials.
	 *		   The list may be empty if no raw materials are found.
	 */
	List<RawMaterialDto> findAll();

	/**
	 * Retrieves a list of all raw material details, including information about suppliers, names in
	 * various languages, measurement, and category type identifiers.
	 *
	 * @return A list of AllRawMaterialSupplierDto containing detailed information about the raw materials
	 *		   and their associated suppliers. The returned list may be empty if no data is available.
	 */
	List<AllRawMaterialSupplierDto> findRawMaterialDetails();

	/**
	 * Checks the existence of an entity with the specified unique identifier.
	 *
	 * @param id The unique identifier of the entity to be checked for existence.
	 * @return A Boolean value indicating whether an entity with the specified ID exists.
	 */
	boolean isExists(Long id);

	/**
	 * Updates the priority of a raw material based on the provided DTO.
	 *
	 * <p>This method performs business logic validation and delegates the update operation
	 * to the data access layer. It may throw a {@link RestException} in case of validation
	 * failure, missing data, or update errors.</p>
	 *
	 * @param list the {@link RawMaterialDto} containing the updated priority and necessary metadata
	 * @throws RestException if the update operation fails due to validation errors or internal issues
	 */
	void updatePriority(List<RawMaterialDto> list) throws RestException;

	/**
	 * Fetches the highest priority value from the raw material records.
	 * <p>
	 * This method queries the database to determine the maximum value of the
	 * {@code priority} field in the {@code raw_material} table.
	 *
	 * @return the highest priority as a {@code Long}, or {@code null} if no records exist
	 * @throws RestException if an error occurs during the retrieval process
	 */
	Long getHighestPriority() throws RestException;

}