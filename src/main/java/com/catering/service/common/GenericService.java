package com.catering.service.common;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.exception.RestException;

/**
 * A generic service interface providing CRUD operations and additional
 * query methods for various types of entities and Data Transfer Objects (DTOs).
 *
 * @param <D> The type representing the Data Transfer Object (DTO).
 * @param <M> The type representing the entity/model.
 * @param <I> The type representing the identifier of the entity.
 */
public interface GenericService<D, M, I> {

	/**
	 * Creates or updates an entity based on the provided DTO and ID. If the ID is null,
	 * a new entity is created; otherwise, an existing entity with the given ID is updated.
	 *
	 * @param dto The DTO containing the data to be saved or updated.
	 * @param dtoType The Class object representing the DTO type.
	 * @param modelType The Class object representing the entity type.
	 * @param id The ID of the entity to be updated, or null for creating a new entity.
	 * @return The DTO representing the created or updated entity.
	 * @throws RestException If an error occurs during the operation, such as validation failures.
	 */
	D createAndUpdate(D dto, Class<D> dtoType, Class<M> modelType, I id) throws RestException;

	/**
	 * Retrieves a list of entities based on the provided filter and sorting criteria.
	 *
	 * @param dtoType The Class object representing the DTO type used for the response.
	 * @param modelType The Class object representing the entity type.
	 * @param filterDto The filter criteria for querying entities.
	 * @param example An optional Example instance used for filtering entities.
	 * @return A ResponseContainerDto containing a list of DTOs representing the entities that match the filter criteria, along with optional paging information.
	 */
	ResponseContainerDto<List<D>> read(Class<D> dtoType, Class<M> modelType, FilterDto filterDto, Optional<Example<M>> example);

	/**
	 * Deletes an entity by its ID.
	 *
	 * @param id The ID of the entity to delete.
	 * @throws RestException If no entity with the given ID exists.
	 */
	void deleteById(I id) throws RestException;

	/**
	 * Retrieves a single entity by its ID.
	 *
	 * @param id The ID of the entity to retrieve.
	 * @param dtoType The Class object representing the DTO type used for the response.
	 * @return An optional containing the DTO representing the found entity, or an empty optional if no entity with the given ID exists.
	 * @throws RestException If the provided ID is null or if the entity is not found.
	 */
	Optional<D> read(I id, Class<D> dtoType) throws RestException;

}