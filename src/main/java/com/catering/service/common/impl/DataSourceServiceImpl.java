package com.catering.service.common.impl;

import com.catering.properties.DataSourceProperties;
import com.catering.service.common.DataSourceService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.util.Objects;

/**
 * Implementation of the {@link DataSourceService} interface for creating and managing
 * DataSource instances using properties defined in {@link DataSourceProperties}.
 *
 * This service utilizes HikariCP for connection pooling and is configured using
 * settings provided by the {@link DataSourceProperties} class.
 *
 * Key configuration details:
 * - JDBC URL: If a custom value is not provided, a default URL is constructed using {@link DataSourceProperties#getDefaultDataSourceUrl()}.
 * - Database credentials: User name and password are source from {@link DataSourceProperties}.
 * - Driver class name: Specifies the database driver to use.
 * - Connection pooling: Configured using properties like maximum pool size and minimum idle connections.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataSourceServiceImpl implements DataSourceService {

	/**
	 * Holds configuration properties for the data source.
	 */
	DataSourceProperties dataSourceProperties;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource build(String jdbcUrl) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(dataSourceProperties.getDriver());
		hikariConfig.setUsername(dataSourceProperties.getUsername());
		hikariConfig.setPassword(dataSourceProperties.getPassword());
		hikariConfig.setJdbcUrl(Objects.isNull(jdbcUrl) ? dataSourceProperties.getDefaultDataSourceUrl() : jdbcUrl);
		hikariConfig.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
		hikariConfig.setMinimumIdle(0);
		return new HikariDataSource(hikariConfig);
	}

}