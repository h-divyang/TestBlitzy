package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.DebitNoteGetByIdDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRequestDto;
import com.catering.dto.tenant.request.DebitNoteResponseDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.model.tenant.DebitNoteModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing debit notes.
 * Provides methods for creating, updating, retrieving, and deleting debit notes,
 * as well as handling associated entities and drop down data.
 */
public interface DebitNoteService extends GenericService<DebitNoteResponseDto, DebitNoteModel, Long> {

	/**
	 * Creates or updates a debit note using the provided details.
	 *
	 * @param debitNoteRequestDto The debit note details to create or update.
	 * @return An Optional containing the created or updated DebitNoteRequestDto, or an empty Optional if the operation fails.
	 */
	Optional<DebitNoteRequestDto> createAndUpdate(DebitNoteRequestDto debitNoteRequestDto);

	/**
	 * Retrieves a list of debit notes based on the provided filter criteria.
	 *
	 * @param filterDto The filter criteria used to retrieve the list of debit notes.
	 * @return A ResponseContainerDto containing a list of DebitNoteResponseDto objects.
	 */
	ResponseContainerDto<List<DebitNoteResponseDto>> read(FilterDto filterDto);

	/**
	 * Retrieves a debit note by its unique identifier.
	 *
	 * @param id The unique identifier of the debit note to be retrieved.
	 * @return The DebitNoteGetByIdDto containing the details of the retrieved debit note.
	 */
	DebitNoteGetByIdDto getById(Long id);

	/**
	 * Deletes a debit note by its unique identifier.
	 *
	 * @param id The unique identifier of the debit note to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of debit note purchase bill drop down data based on the provided identifier.
	 *
	 * @param id The unique identifier for which the debit note purchase bill drop down data is to be retrieved.
	 * @return A ResponseContainerDto containing a list of DebitNotePurchaseBillDropDownDto objects.
	 */
	ResponseContainerDto<List<DebitNotePurchaseBillDropDownDto>> getDebitNotePurchaseBillDropDownData(Long id);

	/**
	 * Retrieves a list of raw material details associated with a purchase bill in the context of a debit note.
	 *
	 * @param id The unique identifier for which the raw material details linked to the purchase bill are to be retrieved.
	 * @return A ResponseContainerDto containing a list of PurchaseBillOrderRawMaterialDto objects that include raw material details.
	 */
	ResponseContainerDto<List<PurchaseBillOrderRawMaterialDto>> getDebitNotePurchaseBillRawMaterial(Long id);

	/**
	 * Retrieves a list of raw material drop down data that can be used in the context of debit notes.
	 *
	 * @return A ResponseContainerDto containing a list of DebitNoteRawMaterialDropDownDto objects.
	 */
	ResponseContainerDto<List<DebitNoteRawMaterialDropDownDto>> getRawMaterialDropDownData();

}