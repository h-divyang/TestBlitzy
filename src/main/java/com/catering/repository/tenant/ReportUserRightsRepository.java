package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.ReportUserRightsModel;

/**
 * Repository interface for managing report user rights in the database.
 * Extends JpaRepository to provide standard CRUD operations for the ReportUserRightsModel entity.
 *
 * Key Responsibilities:
 * - Provides methods to interact with the report_user_rights table.
 * - Supports basic data access operations such as save, delete, and find.
 *
 * Extends:
 * - JpaRepository - Spring Data repository providing generic CRUD operations.
 *
 * @author Krushali Talaviya
 * @since 2025-01-13
 */
public interface ReportUserRightsRepository extends JpaRepository<ReportUserRightsModel, Long> {
}