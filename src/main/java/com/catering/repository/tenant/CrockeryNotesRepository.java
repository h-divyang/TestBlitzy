package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.CrockeryNotesModel;

/**
 * CrockeryNotesRepository is an interface for managing CrockeryNotesModel entities,
 * extending the JpaRepository interface to provide basic CRUD operations and custom query methods.
 *
 * This repository includes a custom native query for retrieving notes related to general raw material fixes
 * based on the provided language type.
 */
public interface CrockeryNotesRepository extends JpaRepository<CrockeryNotesModel, Long> {
}