package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.OrderBookingTemplateNotesModel;

/**
 * Repository interface for managing {@link OrderBookingTemplateNotesModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderBookingTemplateNotesModel}.
 * It allows for querying and manipulating order booking template notes data.
 */
public interface OrderBookingTemplateNotesRepository extends JpaRepository<OrderBookingTemplateNotesModel, Long> {
}