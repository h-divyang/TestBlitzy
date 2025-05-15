package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.PurchaseOrderModel;

/**
 * Repository interface for accessing and manipulating purchase order data.
 * 
 * This interface extends `JpaRepository`, providing built-in CRUD operations for `PurchaseOrderModel`.
 * Additional custom queries can be defined as needed to retrieve and manage purchase order data.
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderModel, Long> {
}