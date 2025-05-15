package com.catering.service.tenant;

import java.util.Optional;
import com.catering.dto.tenant.request.TableMenuReportNotesDto;

/**
 * Service interface responsible for managing header notes for table menu reports.
 * This interface provides methods for retrieving and saving header notes associated with specific orders.
 */
public interface TableMenuReportHeaderNotesService {

	/**
	 * Retrieves the header notes for a table menu report based on the given order ID.
	 *
	 * @param orderId The unique identifier of the order for which the header notes are to be retrieved.
	 * @return A TableMenuReportNotesDto object containing the header notes details for the specified order,
	 *		   or null if no header notes are found for the given order ID.
	 */
	TableMenuReportNotesDto getTableMenuReportHeaderNotes(Long orderId);

	/**
	 * Saves the header notes for a table menu report.
	 *
	 * @param tableMenuReportHeaderNotesDto The TableMenuReportNotesDto object containing the header notes details to be saved.
	 * @return An Optional containing the saved TableMenuReportNotesDto if successful, or an empty Optional otherwise.
	 */
	Optional<TableMenuReportNotesDto> saveTableMenuReportHeaderNotes(TableMenuReportNotesDto tableMenuReportHeaderNotesDto);

}