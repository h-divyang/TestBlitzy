package com.catering.service.tenant;

import java.util.Optional;
import com.catering.dto.tenant.request.OrderBookingTemplateNotesDto;

/**
 * Service interface for managing operations related to order booking report template notes.
 * This interface provides methods to retrieve and save details for default, preferred, and supportive language notes
 * associated with order booking report templates.
 */
public interface OrderBookingReportTemplateNotesService {

	/**
	 * Retrieves the order booking template notes.
	 *
	 * @return An instance of OrderBookingTemplateNotesDto containing the default, preferred, and supportive language notes information.
	 */
	OrderBookingTemplateNotesDto getOrderBookingTemplateNotes();

	/**
	 * Saves the provided order booking template notes details.
	 *
	 * @param orderBookingTemplateNotesDto The OrderBookingTemplateNotesDto object containing the details of the notes to be saved.
	 * @return An Optional containing the saved OrderBookingTemplateNotesDto if successful, or an empty Optional otherwise.
	 */
	Optional<OrderBookingTemplateNotesDto> saveOrderBookingTemplateNotes(OrderBookingTemplateNotesDto orderBookingTemplateNotesDto);

}