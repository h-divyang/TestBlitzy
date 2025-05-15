package com.catering.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.catering.model.superadmin.DataSourceConfigModel;
import com.catering.properties.DataSourceProperties;
import com.catering.repository.superadmin.DataSourceConfigRepository;
import com.catering.service.common.DataSourceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The TenantDataSourceConfig class is responsible for managing data sources for multiple tenants in a multi-tenant application.
 * It provides the functionality to create and retrieve data sources dynamically based on the tenant's configuration.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TenantDataSourceConfig implements Serializable {

	static final long serialVersionUID = 2583528632974825787L;

	/**
	 * A map to store created DataSource instances for each tenant.
	 * The key is the tenant name and the value is the corresponding DataSource.
	 */
	transient HashMap<String, DataSource> dataSources = new HashMap<>();

	/**
	 * The repository to access the data source configuration from the database.
	 */
	transient DataSourceConfigRepository configRepo;

	/**
	 * Contains the properties necessary for constructing the data source URL.
	 */
	transient DataSourceProperties dataSourceProperties;

	/**
	 * A service to build the DataSource from the properties.
	 */
	transient DataSourceService dataSourceService;

	/**
	 * Retrieves the DataSource for the specified tenant.
	 * If the DataSource for the tenant already exists in the cache, it returns it.
	 * If not, it creates and caches the DataSource.
	 * 
	 * @param name The name of the tenant (usually the database name).
	 * @return The DataSource for the given tenant.
	 */
	public DataSource getDataSource(String name) {
		if (Objects.nonNull(dataSources.get(name))) {
			return dataSources.get(name);
		}
		DataSource dataSource = createDataSource(name);
		if (Objects.nonNull(dataSource)) {
			dataSources.put(name, dataSource);
		}
		return dataSource;
	}

	/**
	 * Retrieves all available DataSources by querying the DataSourceConfigRepository.
	 * It constructs a map of tenant names to their corresponding DataSource.
	 * 
	 * @return A map containing all the available DataSources keyed by tenant names.
	 */
	@PostConstruct
	public Map<String, DataSource> getAll() {
		List<DataSourceConfigModel> configList = configRepo.findAll();
		Map<String, DataSource> result = new HashMap<>();
		for (DataSourceConfigModel config : configList) {
			DataSource dataSource = getDataSource(config.getTenant());
			result.put(config.getTenant(), dataSource);
		}
		return result;
	}

	/**
	 * Creates a DataSource for a given tenant name by fetching its configuration from the repository
	 * and constructing a DataSource using the DataSourceService.
	 * 
	 * @param name The name of the tenant for which the DataSource should be created.
	 * @return The created DataSource for the tenant, or null if the configuration is not found.
	 */
	private DataSource createDataSource(String name) {
		DataSourceConfigModel config = configRepo.findByTenant(name);
		return Objects.nonNull(config) ? dataSourceService.build(dataSourceProperties.getUrl() + config.getTenant() + dataSourceProperties.getProperties()) : null;
	}

}