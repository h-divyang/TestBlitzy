package com.catering.repository.tenant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.NotesModel;

/**
 * Repository interface for managing {@link NotesModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations and custom query methods 
 * for managing notes associated with book orders in the application.
 * 
 * Custom methods:
 * - {@link #findByBookOrderId(Long)}: Retrieves a note associated with a specific book order by its ID.
 */
public interface NotesRepository extends JpaRepository<NotesModel, Long> {

	/**
	 * Finds a note associated with a specific book order by its ID.
	 * 
	 * @param orderId the ID of the book order for which the note is associated.
	 * @return an Optional containing the note if found, or empty if no note is found.
	 */
	Optional<NotesModel> findByBookOrderId(Long orderId);

}