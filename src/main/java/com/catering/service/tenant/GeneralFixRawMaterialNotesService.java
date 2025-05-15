package com.catering.service.tenant;

import java.util.Optional;
import com.catering.dto.tenant.request.GeneralFixRawMaterialNotesDto;

/**
 * Service interface for managing general fix raw material notes operations.
 *
 * This interface defines methods for retrieving, saving, or updating details
 * related to general fix raw material notes.
 */
public interface GeneralFixRawMaterialNotesService {

	/**
	 * Retrieves the general fix raw material notes.
	 *
	 * @return An instance of GeneralFixRawMaterialNotesDto containing the general fix raw material notes details.
	 */
	GeneralFixRawMaterialNotesDto getGeneralFixRawMaterialNotes();

	/**
	 * Saves or updates general fix raw material notes.
	 *
	 * @param generalFixRawMaterialNotesDto The GeneralFixRawMaterialNotesDto object containing details of the raw material notes to be saved or updated.
	 * @return An Optional containing the saved or updated GeneralFixRawMaterialNotesDto. If the operation fails, an empty Optional is returned.
	 */
	Optional<GeneralFixRawMaterialNotesDto> saveGeneralFixRawMaterialNotes(GeneralFixRawMaterialNotesDto generalFixRawMaterialNotesDto);

}