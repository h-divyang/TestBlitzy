package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.SaveCustomMenuAllocationTypeModel;

/**
 * Repository interface for performing CRUD operations and interacting with the
 * SaveCustomMenuAllocationTypeModel entity.
 *
 * This interface extends JpaRepository which provides methods such as saving,
 * finding, deleting, and updating SaveCustomMenuAllocationTypeModel entities.
 */
public interface OrderMenuAllocationTypeRepository extends JpaRepository<SaveCustomMenuAllocationTypeModel, Long> {
}