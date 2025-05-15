package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.SaveMenuAllocationOrderMenuPreparationMenuItemModel;

/**
 * Repository interface for managing data access related to
 * {@code SaveMenuAllocationOrderMenuPreparationMenuItemModel}.
 *
 * This interface provides methods to perform CRUD operations and interact
 * with the associated database for menu allocation order menu preparation
 * and menu item entities. It serves as a bridge to facilitate the persistence
 * and retrieval of {@code SaveMenuAllocationOrderMenuPreparationMenuItemModel} entities.
 *
 * It extends the {@code JpaRepository} interface to inherit common JPA functionalities
 * and allows for the definition of custom query methods if required.
 */
public interface SaveAllocationOrderMenuPreparationMenuItemRepository extends JpaRepository<SaveMenuAllocationOrderMenuPreparationMenuItemModel, Long> {
}