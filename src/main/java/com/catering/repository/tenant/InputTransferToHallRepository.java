package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.InputTransferToHallModel;

/**
 * InputTransferToHallRepository is an interface for managing InputTransferToHallModel entities,
 * providing data access functionality through the JpaRepository interface.
 * This repository supports basic CRUD operations and includes custom query methods specific
 * to the InputTransferToHallModel entity.
 */
public interface InputTransferToHallRepository extends JpaRepository<InputTransferToHallModel, Long> {
}