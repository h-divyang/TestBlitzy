package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.LoginLogModel;

/**
 * LoginLogRepository is an interface for managing LoginLogModel entities,
 * providing methods to interact with the underlying database through the JpaRepository interface.
 * This repository supports common CRUD operations and facilitates the management
 * of login logs, including user activity tracking and status recording.
 *
 * It extends JpaRepository to inherit basic data access functionality.
 */
public interface LoginLogRepository  extends JpaRepository<LoginLogModel, Long> {
}