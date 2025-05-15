package com.catering.service.tenant;

import java.util.Optional;
import com.catering.dto.tenant.request.CrockeryNotesDto;

/**
 * Service interface for managing crockery notes. This service provides methods to retrieve and persist
 * crockery notes information.
 */
public interface CrockeryNotesService {

	/**
	 * Fetches the crockery notes information.
	 *
	 * @return An instance of CrockeryNotesDto containing the crockery notes details in various supported languages.
	 */
	CrockeryNotesDto getCrockeryNotes();

	/**
	 * Saves or updates the crockery notes information.
	 *
	 * @param crockeryNotesDto The CrockeryNotesDto object containing information about the crockery notes to be saved or updated.
	 * @return An Optional containing the saved or updated CrockeryNotesDto. If the operation fails, an empty Optional is returned.
	 */
	Optional<CrockeryNotesDto> saveCrockeryNotes(CrockeryNotesDto crockeryNotesDto);

}