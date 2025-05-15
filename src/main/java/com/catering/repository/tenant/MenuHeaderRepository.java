package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MenuHeaderModel;

/**
 * MenuHeaderRepository is an interface that extends JpaRepository to provide CRUD (Create, Read, Update, Delete) operations for MenuHeaderModel entities.
 * It is responsible for interacting with the underlying database or persistence layer to perform operations related to MenuHeaderModel entities.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
public interface MenuHeaderRepository extends JpaRepository<MenuHeaderModel, Long> {
}