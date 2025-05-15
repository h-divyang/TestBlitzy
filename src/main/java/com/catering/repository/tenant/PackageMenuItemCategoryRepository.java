package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.PackageMenuItemCategoryModel;

/**
 * Repository interface for managing package final category entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for the package final category entities.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
public interface PackageMenuItemCategoryRepository extends JpaRepository<PackageMenuItemCategoryModel, Long> {

}