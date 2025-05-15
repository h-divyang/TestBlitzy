package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCustomDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactModel;
import com.catering.service.common.GenericService;

/**
 * The ContactService interface provides operations for managing contacts within the system.
 * It extends the GenericService interface to inherit common CRUD operations while adding contact-specific functionalities.
 */
public interface ContactService extends GenericService<ContactResponseDto, ContactModel, Long> {

	/**
	 * Creates a new contact or updates an existing contact based on the provided details.
	 *
	 * @param contactDto The data transfer object containing the details of the contact to be created or updated.
	 * @return An Optional containing the updated or created contact details, or an empty Optional if the operation is unsuccessful.
	 * @throws RestException If any error occurs during the creation or update process.
	 */
	Optional<ContactResponseDto> createAndUpdate(ContactResponseDto contactDto) throws RestException;

	/**
	 * Checks the existence of an entity by its ID. If the entity does not exist, a RestException is thrown.
	 *
	 * @param id The unique identifier of the entity to check for existence.
	 * @throws RestException If the entity does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Retrieves a list of contacts based on the specified filter criteria.
	 *
	 * @param filterDto The filter criteria used to query the contact data.
	 * @return A ResponseContainerDto containing a list of ContactResponseDto objects that meet the filter criteria.
	 */
	ResponseContainerDto<List<ContactResponseDto>> read(FilterDto filterDto);

	/**
	 * Deletes an entity identified by the given ID.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Checks the existence of a ContactCategory entity with the specified ID.
	 *
	 * @param id The unique identifier of the ContactCategory entity to check.
	 * @return A boolean indicating whether a ContactCategory entity with the given ID exists (true) or not (false).
	 */
	boolean existsByContactCategoryId(Long id);

	/**
	 * Retrieves a unique list of contact response data transfer objects filtered and processed based on the given parameters.
	 *
	 * @param dtoType The class type of the data transfer object (ContactResponseDto).
	 * @param modelType The class type of the model entity (ContactModel).
	 * @param IDs The list of IDs used to filter the contacts.
	 * @param rawMaterialId The identifier for the raw material associated with the requested data.
	 * @param userEditId The user ID responsible for editing or filtering the data.
	 * @return A ResponseContainerDto object containing a list of unique ContactResponseDto objects.
	 */
	ResponseContainerDto<List<ContactResponseDto>> uniqueList(Class<ContactResponseDto> dtoType, Class<ContactModel> modelType, List<Long> IDs, Long rawMaterialId, Long userEditId);

	/**
	 * Checks if an entity exists by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to check for existence.
	 * @return True if an entity with the given ID exists, false otherwise.
	 */
	boolean existById(Long id);

	/**
	 * Retrieves a list of contacts based on the specified category type identifier.
	 *
	 * @param categoryTypeId The unique identifier of the category type for which contacts should be retrieved.
	 * @return A list of ContactResponseDto objects matching the specified category type, or an empty list if no matches are found.
	 */
	List<ContactResponseDto> findByCategoryType(Long categoryTypeId);

	/**
	 * Retrieves a list of active contacts that belong to a specific contact category.
	 *
	 * @param contactCategoryId The unique identifier of the contact category.
	 * @return A list of ContactResponseDto objects representing the active contacts within the specified category.
	 */
	List<ContactResponseDto> findByIsActiveTrueAndContactCategoryId(Long contactCategoryId);

	/**
	 * Retrieves a list of all available contacts.
	 *
	 * @return A list of ContactCustomDto objects representing all contacts.
	 */
	List<ContactCustomDto> findAllContact();

	/**
	 * Retrieves a list of all available bank contacts.
	 *
	 * @return A list of ContactCustomDto objects representing all bank contacts.
	 */
	List<ContactCustomDto> findAllBankList();

}