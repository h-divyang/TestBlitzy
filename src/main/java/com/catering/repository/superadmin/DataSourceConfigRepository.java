package com.catering.repository.superadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.superadmin.DataSourceConfigModel;

/**
 * Repository interface for managing data source configuration data.
 *
 * Extends the {@link JpaRepository} to provide basic CRUD operations and query method
 * support for the {@code DataSourceConfigModel} entity. This interface facilitates
 * interaction with the database table associated with data source configurations.
 */
public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfigModel, Long> {

	/**
	 * Finds a DataSourceConfigModel entity based on the tenant name.
	 *
	 * @param tenant The name of the tenant to search for
	 * @return The corresponding DataSourceConfigModel for the specified tenant,
	 *         or null if no matching tenant is found.
	 */
	DataSourceConfigModel findByTenant(String tenant);

}