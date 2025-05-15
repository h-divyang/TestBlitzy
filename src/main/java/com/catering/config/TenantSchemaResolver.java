package com.catering.config;

import java.util.Objects;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import com.catering.properties.DataSourceProperties;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The TenantSchemaResolver class is responsible for resolving the current tenant's database name
 * based on the request context. It implements the CurrentTenantIdentifierResolver interface, which 
 * is used by Hibernate to determine the current tenant's schema or database.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

	/**
	 * The DataSourceProperties object contains properties related to the data source configuration.
	 * It is used to fetch the default database name when a tenant is not explicitly set.
	 */
	DataSourceProperties dataSourceProperties;

	/**
	 * Resolves the current tenant identifier (i.e., the database name) for the particular request.
	 * It checks the TenantContext to get the current tenant. If no tenant is set, it returns the default database name
	 * from the DataSourceProperties.
	 *
	 * @return The database name of the current tenant, or the default database name if no tenant is set.
	 */
	@Override
	public String resolveCurrentTenantIdentifier() {
		String t = TenantContext.getCurrentTenant();
		if (Objects.nonNull(t)) {
			return t;
		} else {
			return dataSourceProperties.getName();
		}
	}

	/**
	 * Validates if existing sessions for the current tenant are valid.
	 * 
	 * @return Always returns true, indicating that existing sessions are considered valid.
	 */
	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}