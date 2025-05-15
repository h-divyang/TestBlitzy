package com.catering.service.tenant;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import com.catering.dto.tenant.request.FlagDto;
import com.catering.dto.tenant.request.NotesDto;

/**
 * Interface representing a service for managing notes operations.
 * This service provides methods for creating, retrieving, and managing configurations related to notes and reports.
 *
 * Implementations of this interface are expected to provide the business logic
 * for handling notes functionalities in the application.
 */
public interface NotesService {

	/**
	 * Saves the provided notes details.
	 *
	 * @param notesDto The NotesDto object containing details of the notes to be saved.
	 * @return An Optional containing the saved NotesDto if successful, or an empty Optional otherwise.
	 */
	Optional<NotesDto> saveNotes(NotesDto notesDto);

	/**
	 * Retrieves the notes associated with a specific order.
	 *
	 * @param orderId The unique identifier of the order for which notes are to be retrieved.
	 * @return A NotesDto object containing the notes details related to the given order ID.
	 */
	NotesDto getNotes(Long orderId);

	/**
	 * Retrieves the flag configuration for visibility of notes and table menu reports.
	 *
	 * @param request The HttpServletRequest object containing information about the current HTTP request.
	 * @return A FlagDto object containing boolean flags indicating visibility settings for notes and table menu reports.
	 */
	FlagDto getFlagForNotesAndReports(HttpServletRequest request);

}