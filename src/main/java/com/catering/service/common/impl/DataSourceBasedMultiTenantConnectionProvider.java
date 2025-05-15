package com.catering.service.common.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.catering.config.TenantDataSourceConfig;
import com.catering.model.superadmin.DataSourceConfigModel;
import com.catering.properties.DataSourceProperties;
import com.catering.service.common.DataSourceService;

@Component
public class DataSourceBasedMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = 8375460738838958359L;
	private static final String DEFAULT_DATABASE = "catering";

	@Autowired
	private transient DataSource defaultDataSource;

	@Autowired
	private transient ApplicationContext context;

	private transient Map<String, DataSource> map = new HashMap<>();

	boolean init = false;

	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Autowired
	private DataSourceService dataSourceService;

	@PostConstruct
	public void load() {
		map.put(DEFAULT_DATABASE, defaultDataSource);
	}

	@Override
	protected DataSource selectAnyDataSource() {
		return map.get(DEFAULT_DATABASE);
	}

	/**
	 * Select current DataSouce based on name
	 * Default Database will be selected if DataSource not found by given name
	 * */
	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		if (!init) {
			init = true;
			TenantDataSourceConfig tenantDataSource = context.getBean(TenantDataSourceConfig.class);
			map.putAll(tenantDataSource.getAll());
		}
		if (!map.containsKey(tenantIdentifier)) {
			map.put(tenantIdentifier, dataSourceService.build(dataSourceProperties.getUrl() + tenantIdentifier + dataSourceProperties.getProperties()));
		}
		return Objects.nonNull(map.get(tenantIdentifier)) ? map.get(tenantIdentifier) : map.get(DEFAULT_DATABASE);
	}

	/**
	 * Add DataSource in the current Map
	 * This will execute after registered company
	 * */
	public DataSource addDataSource(DataSourceConfigModel dataSourceConfig) {
		TenantDataSourceConfig tenantDataSource = context.getBean(TenantDataSourceConfig.class);
		DataSource dataSource = tenantDataSource.getDataSource(dataSourceConfig.getTenant());
		map.put(dataSourceConfig.getTenant(), dataSource);
		return dataSource;
	}

}