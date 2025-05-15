package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.ExtraExpenseDto;
import com.catering.dto.tenant.request.ExtraExpenseParameterDto;
import com.catering.model.tenant.ExtraExpenseModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing ExtraExpense entities and their corresponding DTOs.
 * This interface extends the GenericService interface and provides additional
 * methods specific to the ExtraExpense domain.
 */
public interface ExtraExpenseService extends GenericService<ExtraExpenseDto, ExtraExpenseModel, Long> {

	/**
	 * Retrieves a list of ExtraExpenseDto objects based on the provided order function ID.
	 *
	 * @param parameterDto An ExtraExpenseParameterDto object containing the order function ID.
	 * @return A list of ExtraExpenseDto objects that are associated with the given order function ID.
	 */
	List<ExtraExpenseDto> findByOrderFunctionId(ExtraExpenseParameterDto parameterDto);

	/**
	 * Creates a new extra expense or updates an existing one based on the provided ExtraExpenseDto.
	 *
	 * @param extraExpenseDto An ExtraExpenseDto object containing the details of the extra expense to create or update.
	 * @return The created or updated ExtraExpenseDto object.
	 */
	ExtraExpenseDto createAndUpdate(ExtraExpenseDto extraExpenseDto);

	/**
	 * Deletes an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

}