package com.catering.service.common;

import javax.sql.DataSource;

/**
 * Service interface for building and managing DataSource instances.
 */
public interface DataSourceService {

	/**
	 * Builds and returns a DataSource object based on the provided JDBC URL.
	 *
	 * @param jdbcUrl The JDBC URL used to configure and construct the DataSource.
	 * @return A DataSource instance configured with the specified JDBC URL.
	 */
	DataSource build(String jdbcUrl);

}