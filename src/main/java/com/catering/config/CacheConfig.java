package com.catering.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Configuration class to enable and configure caching with Caffeine.
 * <p>
 * This class sets up the caching mechanism using the Caffeine library, which provides an in-memory cache
 * with configurable eviction policies. It configures a cache manager that utilizes a Caffeine cache instance 
 * for caching purposes.
 * </p>
 * <p>
 * The following cache configuration is applied:
 * <ul>
 * <li><b>Expiration policy:</b> Cache entries will expire 24 hours after they are written.</li>
 * </ul>
 * </p>
 * <p>
 * This configuration class is annotated with {@link EnableCaching} to enable Spring's caching support 
 * and register the caching beans defined in the class.
 * </p>
 * 
 * @see Caffeine
 * @see CaffeineCacheManager
 * @see CacheManager
 */
@Configuration
@EnableCaching
public class CacheConfig {

	/**
	 * Creates a Caffeine cache configuration with a specified expiration policy.
	 * <p>
	 * This bean defines the caching behavior using Caffeine, with cache entries expiring 24 hours 
	 * after being written to the cache.
	 * </p>
	 *
	 * @return a configured Caffeine cache instance
	 */
	@Bean
	Caffeine<Object, Object> caffeineConfig() {
		return Caffeine.newBuilder().expireAfterWrite(24, TimeUnit.HOURS);
	}

	/**
	 * Creates and configures a CacheManager to manage caches with the Caffeine cache instance.
	 * <p>
	 * This bean sets the previously created Caffeine configuration into a CaffeineCacheManager, which 
	 * is then used by Spring's caching infrastructure to manage the caching behavior across the application.
	 * </p>
	 *
	 * @param caffeine the Caffeine cache configuration
	 * @return a CacheManager instance that uses Caffeine for caching
	 */
	@Bean
	CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
		caffeineCacheManager.setCaffeine(caffeine);
		return caffeineCacheManager;
	}

}