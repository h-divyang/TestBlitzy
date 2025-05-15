package com.catering.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up ModelMapper in a Spring application.
 * <p>
 * This configuration class provides the necessary setup to create and configure a {@link ModelMapper} bean, 
 * which is used to map objects between different object models (e.g., DTOs and entities).
 * </p>
 * <p>
 * The `ModelMapper` is configured with a strict matching strategy, ensuring that the properties in source and destination objects 
 * must match exactly (in both name and type) for the mapping to occur. This minimizes mapping errors and ensures consistency.
 * </p>
 * 
 * @see ModelMapper
 * @see MatchingStrategies
 */
@Configuration
public class ModelMapperConfig {

	/**
	 * Creates and configures a ModelMapper bean.
	 * <p>
	 * This method creates an instance of {@link ModelMapper}, configures it with a strict matching strategy 
	 * (where source and destination properties must match exactly), and returns the configured instance.
	 * </p>
	 * 
	 * @return the configured {@link ModelMapper} instance
	 */
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper =  new ModelMapper();
		// Set the matching strategy to STRICT, meaning that only exact matches will be considered for mapping
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

}