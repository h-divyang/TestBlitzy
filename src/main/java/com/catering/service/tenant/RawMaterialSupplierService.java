package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.RawMaterialSupplierDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialSupplierModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing raw material suppliers. It provides methods to create, update,
 * retrieve, set default, and delete raw material supplier entities.
 * Extends the GenericService interface for common CRUD operations.
 */
public interface RawMaterialSupplierService extends GenericService<RawMaterialSupplierDto, RawMaterialSupplierModel, Long> {

	/**
	 * Creates or updates a Raw Material Supplier entity based on the provided data.
	 *
	 * @param rawMaterialSupplierDto The RawMaterialSupplierDto object containing the details of the supplier to create or update.
	 * @return An Optional containing the created or updated RawMaterialSupplierDto object if successful, or an empty Optional if the operation fails.
	 * @throws RestException if there is an error during the create or update operation.
	 */
	Optional<RawMaterialSupplierDto> createAndUpdate(RawMaterialSupplierDto rawMaterialSupplierDto) throws RestException;

	/**
	 * Retrieves a list of raw material suppliers based on the provided search and pagination parameters.
	 *
	 * @param rawMaterialId The unique identifier of the raw material whose suppliers are to be retrieved.
	 * @param page The page number to retrieve (used for pagination).
	 * @param size The number of records per page (used for pagination).
	 * @param sortBy The field by which the results should be sorted.
	 * @param sortDirection The direction of sorting (e.g., "asc" for ascending or "desc" for descending).
	 * @param query An optional search query to filter suppliers based on specific criteria.
	 * @return A ResponseContainerDto containing a list of RawMaterialSupplierDto objects and any associated metadata.
	 */
	ResponseContainerDto<List<RawMaterialSupplierDto>> read(Long rawMaterialId, String page, String size, String sortBy, String sortDirection, String query);

	/**
	 * Marks or updates the default status of a raw material supplier based on the provided parameters.
	 *
	 * @param rawMaterialId The unique identifier of the raw material to which the supplier is related.
	 * @param contractorId The unique identifier of the contractor or supplier to be marked as default or not.
	 * @param isDefault A boolean flag indicating whether to mark the supplier as default (true) or not default (false).
	 * @return An Optional containing the updated RawMaterialSupplierDto object if the operation is successful, or an empty Optional if it fails.
	 * @throws RestException if there is an error during the operation.
	 */
	Optional<RawMaterialSupplierDto> markDefault(Long rawMaterialId, Long contractorId, Boolean isDefault) throws RestException;

	/**
	 * Deletes an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

}