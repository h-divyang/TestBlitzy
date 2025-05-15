package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MainMenuModel;

/**
 * MainMenuRepository is an interface that extends JpaRepository to provide CRUD (Create, Read, Update, Delete) operations for MainMenuModel entities.
 * It is responsible for interacting with the underlying database or persistence layer to perform operations related to MainMenuModel entities.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
public interface MainMenuRepository extends JpaRepository<MainMenuModel, Long> {
}