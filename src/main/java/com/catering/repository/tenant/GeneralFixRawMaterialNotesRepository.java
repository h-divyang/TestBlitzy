package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.GeneralFixRawMaterialNotesModel;

/**
 * GeneralFixRawMaterialNotesRepository serves as a data access layer for the
 * GeneralFixRawMaterialNotesModel entity. It extends JpaRepository to provide
 * standard methods for CRUD (Create, Read, Update, Delete) operations on the
 * GeneralFixRawMaterialNotesModel, and also defines a custom method for retrieving
 * specific language-based notes.
 *
 * This repository interacts with the general_fix_raw_material_notes table in the
 * database, managing entities that represent notes data related to fixed raw materials.
 */
public interface GeneralFixRawMaterialNotesRepository extends JpaRepository<GeneralFixRawMaterialNotesModel, Long> {
}