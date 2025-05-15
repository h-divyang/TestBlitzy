package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.ContactCategoryTypeModel;

/**
 * ContactCategoryTypeRepository is an interface for performing CRUD (Create, Read, Update, Delete)
 * operations on the ContactCategoryTypeModel entity.
 *
 * This repository extends JpaRepository, which provides built-in methods for interacting with the
 * persistence layer, enabling seamless data management for the ContactCategoryTypeModel entity.
 */
public interface ContactCategoryTypeRepository extends JpaRepository<ContactCategoryTypeModel, Long> {
}