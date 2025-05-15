package com.catering.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class to manage the current tenant context in a multi-tenant application.
 * <p>
 * This class leverages {@link ThreadLocal} to store the current tenant (database name or identifier)
 * for the current thread of execution. The context is specific to each thread (or request), ensuring
 * that different requests can have different tenants (databases) without interference.
 * </p>
 * <p>
 * The tenant context is typically set early in the request lifecycle (e.g., by a filter or interceptor)
 * and cleared after the request is processed. This allows the application to determine which database to
 * connect to based on the tenant information provided.
 * </p>
 * 
 * @see ThreadLocal
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantContext {

	private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

	/**
	 * Get current Database name which is connected for particular request
	 * */
	public static String getCurrentTenant() {
		return currentTenant.get();
	}

	/**
	 * Set current Database name which will connect for particular request
	 * */
	public static void setCurrentTenant(String tenant) {
		currentTenant.set(tenant);
	}

	/**
	 * Remove current Database name which is connected for particular request
	 * */
	public static void clear() {
		currentTenant.remove();
	}

}