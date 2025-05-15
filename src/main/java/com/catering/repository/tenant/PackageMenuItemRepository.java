package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.PackageMenuItemModel;

/**
 * Repository interface for managing package final material entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for the package final material entities.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
public interface PackageMenuItemRepository extends JpaRepository<PackageMenuItemModel, Long> {

}