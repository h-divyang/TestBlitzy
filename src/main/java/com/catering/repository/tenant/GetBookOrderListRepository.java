package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.GetBookOrderListModel;

/**
 * GetBookOrderListRepository is an interface for performing CRUD (Create, Read, Update, Delete)
 * operations on the GetBookOrderListModel entity.
 * This interface extends JpaRepository, which provides methods to interact with the database
 * such as saving, deleting, and finding entities.
 *
 * This repository serves as the data access layer for the GetBookOrderListModel entity,
 * enabling consistent and efficient interaction with the underlying persistence layer.
 */
public interface GetBookOrderListRepository extends JpaRepository<GetBookOrderListModel, Long> {
}