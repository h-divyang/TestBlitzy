package com.catering.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.catering.service.common.DataSourceService;

/**
 * Configuration class to set up the data source for database connectivity.
 * <p>
 * This class defines the configuration for establishing a connection to the data source
 * using a {@link DataSourceService} to build the data source instance.
 * </p>
 * <p>
 * The {@link DataSource} bean is marked as {@code @Primary} to ensure it is the default data 
 * source when multiple data sources exist in the Spring context.
 * </p>
 * <p>
 * The class includes a commented-out bean definition for the connection, which can be used
 * for a more complex configuration in the future. This can be used to implement more specific
 * connection management strategies, such as dynamic selection of data sources.
 * </p>
 * 
 * @see DataSource
 * @see DataSourceService
 */
@Configuration
public class DataSourceConfig {

	/**
	 * Creates a {@link DataSource} bean that is the primary data source in the application.
	 * <p>
	 * This method uses the {@link DataSourceService} to build the data source instance. The 
	 * {@code null} parameter indicates the data source configuration is using the default settings
	 * in the service. The bean is marked as {@code @Primary}, so it will be injected by default
	 * when a {@link DataSource} is needed.
	 * </p>
	 * 
	 * @param dataSourceService the service used to build the data source
	 * @return a configured {@link DataSource} instance
	 */
	@Bean
	@Primary
	DataSource dataSource(DataSourceService dataSourceService) {
		return dataSourceService.build(null);
	}

}