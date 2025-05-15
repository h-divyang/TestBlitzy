package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.JournalVoucherModel;

/**
 * JournalVoucherRepository is an interface for managing JournalVoucherModel entities,
 * providing methods to perform CRUD operations and interact with the database.
 *
 * This repository extends JpaRepository to inherit basic data access functionalities,
 * such as saving, updating, deleting, and retrieving JournalVoucherModel entities.
 */
public interface JournalVoucherRepository extends JpaRepository<JournalVoucherModel, Long> {
}