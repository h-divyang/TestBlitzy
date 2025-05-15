package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.UserRightsModel;

/**
 * Repository interface for managing user rights and permissions.
 * This interface extends the Spring Data JPA {@link JpaRepository} and is used
 * to perform CRUD operations on {@link UserRightsModel} entities in the database.
 *
 * <p>The repository provides methods for accessing and manipulating user access control settings
 * for various menus and actions within an application.</p>
 *
 * @see JpaRepository
 *
 * @author Krushali Talaviya
 * @since August 2023
 *
 */
public interface UserRightsRepository extends JpaRepository<UserRightsModel, Long> {
}