package com.catering.service.tenant;

import java.util.Optional;
import com.catering.dto.tenant.request.TableMenuReportNotesDto;

/**
 * Interface representing the service for managing footer notes of a table menu report.
 * This service provides methods to retrieve and save footer notes associated with
 * a table menu report by order identifier.
 */
public interface TableMenuReportFooterNotesService {

	/**
	 * Retrieves the footer notes for a table menu report based on the provided order ID.
	 *
	 * @param orderId The unique identifier of the order for which the footer notes are to be retrieved.
	 * @return A TableMenuReportNotesDto object containing the footer notes for the table menu report
	 *		   associated with the given order ID.
	 */
	TableMenuReportNotesDto getTableMenuReportFooterNotes(Long orderId);

	/**
	 * Saves the footer notes associated with a table menu report.
	 *
	 * @param tableMenuReportFooterNotesDto The TableMenuReportNotesDto object containing the details
	 *										of the footer notes to be saved or updated.
	 * @return An Optional containing the saved or updated TableMenuReportNotesDto if successful,
	 *		   or an empty Optional if the operation fails.
	 */
	Optional<TableMenuReportNotesDto> saveTableMenuReportFooterNotes(TableMenuReportNotesDto tableMenuReportFooterNotesDto);

}