package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.RawMaterialCategoryDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialCategoryModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing raw material categories. Extends the generic service interface
 * to provide CRUD operations and additional methods specific to raw material categories.
 */
public interface RawMaterialCategoryService extends GenericService<RawMaterialCategoryDto, RawMaterialCategoryModel, Long> {

	/**
	 * Creates or updates a raw material category based on the provided data.
	 *
	 * @param ietmCategoryDto The RawMaterialCategoryDto object containing the details of the raw material category to be created or updated.
	 * @return An Optional containing the created or updated RawMaterialCategoryDto
	 *		   if the operation is successful, or an empty Optional if it fails.
	 * @throws RestException If an error occurs during the operation.
	 */
	Optional<RawMaterialCategoryDto> createAndUpdate(RawMaterialCategoryDto ietmCategoryDto) throws RestException;

	/**
	 * Verifies if an entity exists by its unique identifier and throws a {@link RestException} if it does not exist.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @throws RestException If the entity with the specified ID does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Reads a list of raw material categories based on the provided filtering criteria.
	 *
	 * @param filterDto The filter criteria for retrieving raw material categories.
	 * @return A {@link ResponseContainerDto} containing the list of {@link RawMaterialCategoryDto} objects
	 *		   that match the provided filter criteria, along with metadata and response status.
	 */
	ResponseContainerDto<List<RawMaterialCategoryDto>> read(FilterDto filterDto);

	/**
	 * Deletes an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of raw material category data that are marked as active.
	 *
	 * @return A list of {@code RawMaterialCategoryDto} objects representing the raw material categories that are active.
	 */
	List<RawMaterialCategoryDto> readDataByIsActive();

	/**
	 * Retrieves a list of raw material categories based on the given category type ID.
	 *
	 * @param categoryId The unique identifier of the category type to filter raw material categories.
	 * @return A list of RawMaterialCategoryDto objects associated with the specified category type ID.
	 */
	List<RawMaterialCategoryDto> readByCategoryTypeId(List<Long> categoryTypeIds);

	/**
	 * Finds a RawMaterialCategoryModel by its unique identifier.
	 *
	 * @param categoryId The unique identifier of the raw material category to be retrieved.
	 * @return The RawMaterialCategoryModel associated with the specified ID, or null if no such entity is found.
	 */
	RawMaterialCategoryModel findById(Long categoryId);

	/**
	 * Retrieves a list of raw material categories where the associated item raw materials are not null.
	 * The results are sorted by their priority in ascending order.
	 *
	 * @return A list of RawMaterialCategoryDto objects representing the raw material categories that meet the criteria, ordered by their priority.
	 */
	List<RawMaterialCategoryDto> findByItemRawMaterialsNotNullOrderByPriority();

	/**
	 * Updates the priority of a raw material category based on the provided DTO.
	 *
	 * <p>This method performs business logic validation and delegates the update operation
	 * to the data access layer. It may throw a {@link RestException} in case of validation
	 * failure, missing data, or update errors.</p>
	 *
	 * @param rawMaterialCategoryDto the {@link RawMaterialCategoryDto} containing the updated priority and necessary metadata
	 * @throws RestException if the update operation fails due to validation errors or internal issues
	 */
	void updatePriority(List<RawMaterialCategoryDto> rawMaterialCategories) throws RestException;

	/**
	 * Fetches the highest priority value from the raw material category records.
	 * <p>
	 * This method queries the database to determine the maximum value of the
	 * {@code priority} field in the {@code raw_material_category} table.
	 *
	 * @return the highest priority as a {@code Long}, or {@code null} if no records exist
	 * @throws RestException if an error occurs during the retrieval process
	 */
	Long getHighestPriority() throws RestException;

}