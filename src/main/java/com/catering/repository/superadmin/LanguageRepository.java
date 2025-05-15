package com.catering.repository.superadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.superadmin.LanguageModel;

/**
 * Repository interface for managing language data.
 *
 * Extends {@link JpaRepository} to provide basic CRUD operations and query method support
 * for the {@code LanguageModel} entity. This interface supports interactions
 * with the database table associated with language-related information.
 */
public interface LanguageRepository extends JpaRepository<LanguageModel, Long> {
}