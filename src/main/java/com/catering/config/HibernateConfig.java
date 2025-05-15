package com.catering.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Configuration class for Hibernate setup and JPA properties customization.
 * <p>
 * This class provides the necessary configuration for Hibernate in a Spring-based application,
 * including the setup of the {@link LocalContainerEntityManagerFactoryBean} and {@link JpaVendorAdapter}.
 * It also configures multi-tenancy support and custom JPA properties.
 * </p>
 * <p>
 * The class defines two main beans:
 * <ul>
 *     <li><b>{@link JpaVendorAdapter}</b> - Configures Hibernate as the JPA provider.</li>
 *     <li><b>{@link LocalContainerEntityManagerFactoryBean}</b> - Configures the EntityManagerFactory with multi-tenancy support.</li>
 * </ul>
 * </p>
 * 
 * @see LocalContainerEntityManagerFactoryBean
 * @see JpaVendorAdapter
 * @see MultiTenantConnectionProvider
 * @see CurrentTenantIdentifierResolver
 */
@Configuration
public class HibernateConfig {

	/**
	 * Configures the {@link JpaVendorAdapter} to use Hibernate as the JPA provider.
	 * 
	 * @return a {@link JpaVendorAdapter} configured to use Hibernate.
	 */
	@Bean
	JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	/**
	 * Configures the {@link LocalContainerEntityManagerFactoryBean} with multi-tenancy support
	 * and customized JPA properties.
	 * <p>
	 * This method sets up Hibernate for multi-tenancy by configuring a {@link MultiTenantConnectionProvider}
	 * and a {@link CurrentTenantIdentifierResolver}. It also customizes JPA properties to support multi-tenancy 
	 * with a {@link MultiTenancyStrategy#DATABASE} strategy.
	 * </p>
	 * 
	 * @param dataSource the data source to use for database connections
	 * @param multiTenantConnectionProviderImpl the multi-tenant connection provider
	 * @param currentTenantIdentifierResolverImpl the tenant identifier resolver
	 * @param jpaProperties the general JPA properties to be customized
	 * @return a configured {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
			CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl, JpaProperties jpaProperties) {
		Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
		jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE.name());
		jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
		jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.catering*");
		em.setJpaVendorAdapter(this.jpaVendorAdapter());
		em.setJpaPropertyMap(jpaPropertiesMap);
		return em;
	}

}