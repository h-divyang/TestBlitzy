package com.catering.repository.superadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.superadmin.ContactCategoryTypeMasterModel;

/**
 * Repository interface for managing contact category type master data.
 *
 * Extends the {@link JpaRepository} to provide basic CRUD operations
 * and query method support for the {@code ContactCategoryTypeMasterModel} entity.
 * This interface enables interaction with the database table associated
 * with contact category type master data.
 */
public interface ContactCategoryTypeMasterRepository extends JpaRepository<ContactCategoryTypeMasterModel, Long> {
}