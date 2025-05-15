package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.AboutUsModel;

/**
 * AboutUsRepository is an interface for performing CRUD (Create, Read, Update, Delete)
 * operations on the AboutUsModel entity.
 * This interface extends JpaRepository, which provides basic methods for interacting
 * with the underlying persistence layer.
 *
 * This repository serves as the data access layer for managing AboutUsModel entities,
 * allowing seamless interaction with the database.
 */
public interface AboutUsRepository extends JpaRepository<AboutUsModel, Long> {
}