package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCategoryDto;
import com.catering.dto.tenant.request.ContactCategoryWithContactsDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactCategoryModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing contact categories. Provides methods for creating,
 * updating, retrieving, and managing contact categories, as well as performing related
 * operations with filtering and validation.
 */
public interface ContactCategoryService extends GenericService<ContactCategoryDto, ContactCategoryModel, Long> {

	/**
	 * Creates or updates a contact category based on the provided ContactCategoryDto.
	 *
	 * @param contactCategoryDto The contact category data to create or update.
	 * @return An Optional containing the created or updated ContactCategoryDto if the operation was successful,
	 *		   or an empty Optional if the operation failed.
	 * @throws RestException If an error occurs during the creation or update process.
	 */
	Optional<ContactCategoryDto> createAndUpdate(ContactCategoryDto contactCategoryDto) throws RestException;

	/**
	 * Retrieves a list of contact categories based on the provided filtering criteria.
	 *
	 * @param filterDto The filter criteria for retrieving contact categories, including pagination and sorting information.
	 * @return A ResponseContainerDto containing the list of ContactCategoryDto objects and associated metadata.
	 */
	ResponseContainerDto<List<ContactCategoryDto>> read(FilterDto filterDto);

	/**
	 * Deletes an entity identified by the given ID.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Checks if an entity with the specified ID exists and is marked as active.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @return true if the entity exists and is active, otherwise false.
	 */
	boolean existByIdAndIsActiveTrue(Long id);

	/**
	 * Validates if an entity with the specified ID exists. If it does not exist, an exception is thrown.
	 *
	 * @param id The unique identifier of the entity to validate.
	 */
	void existByIdOrThrow(Long id);

	/**
	 * Retrieves a list of active ContactCategory data transfer objects (DTOs) ordered by priority in ascending order.
	 * If multiple records have the same priority, they are further sorted by their ID in ascending order.
	 *
	 * @return a list of ContactCategoryDto objects where the "isActive" status is true, 
	 *         ordered by priority (ascending) and ID (ascending).
	 */
	List<ContactCategoryDto> findByIsActiveTrueOrderByPriorityAscIdAsc();

	/**
	 * Retrieves a list of contact categories based on the specified category type ID.
	 *
	 * @param categoryTypeId The unique identifier of the category type to filter contact categories.
	 * @return A list of ContactCategoryDto objects associated with the given category type.
	 */
	List<ContactCategoryDto> findByCategoryType(Long categoryTypeId);

	/**
	 * Retrieves a list of active contact categories filtered by the specified contact category type ID.
	 *
	 * @param contactCategoryTypeId The unique identifier of the contact category type to filter active contact categories.
	 * @return A list of ContactCategoryDto objects where the "isActive" status is true and the contact category type matches the given ID.
	 */
	List<ContactCategoryDto> findByIsActiveTrueAndContactCategoryTypeId(Long contactCategoryTypeId);

	/**
	 * Retrieves a list of active contact categories with their associated active contacts.
	 *
	 * It will only include contact categories where "isActive" is true and the contacts within those
	 * categories also have an "isActive" status of true.
	 *
	 * @param contactCategoryTypeId The unique identifier of the contact category type used to filter the results.
	 * @return A list of ContactCategoryWithContactsDto objects where both the contact categories and their associated contacts are active.
	 */
	List<ContactCategoryWithContactsDto> findByIsActiveTrueAndContactIsActiveTrue(Long contactCategoryTypeId);

	/**
	 * Updates the priority of a contact category based on the provided DTO.
	 *
	 * <p>This method performs business logic validation and delegates the update operation
	 * to the data access layer. It may throw a {@link RestException} in case of validation
	 * failure, missing data, or update errors.</p>
	 *
	 * @param contactCategoryDto the {@link ContactCategoryDto} containing the updated priority and necessary metadata
	 * @throws RestException if the update operation fails due to validation errors or internal issues
	 */
	void updatePriority(List<ContactCategoryDto> contactCategories) throws RestException;

	/**
	 * Fetches the highest priority value from the contact category records.
	 * <p>
	 * This method queries the database to determine the maximum value of the
	 * {@code priority} field in the {@code contact_category} table.
	 *
	 * @return the highest priority as a {@code Long}, or {@code null} if no records exist
	 * @throws RestException if an error occurs during the retrieval process
	 */
	Long getHighestPriority() throws RestException;

}