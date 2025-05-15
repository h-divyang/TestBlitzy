package com.catering.service.common.impl;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.catering.service.common.ModelMapperService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the ModelMapperService interface for object mapping and conversion.
 * This service leverages the ModelMapper utility to simplify and streamline the process of mapping
 * properties between objects, commonly used for conversions between entities and DTOs.
 *
 * The service provides methods to convert individual objects and lists of objects with transformation
 * capabilities.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component, enabling it to be managed by Spring's application context.
 * - @RequiredArgsConstructor: Automatically generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures the default access level for fields to private and makes fields final to ensure immutability.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelMapperServiceImpl implements ModelMapperService {

	/**
	 * Utility for mapping objects between different data models.
	 */
	ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 * See {@link ModelMapperService#convertEntityAndDto(Object, Class)} for details.
	 */
	public <S, D> D convertEntityAndDto(S source, Class<D> destinationType) {
		return modelMapper.map(source, destinationType);
	}

	/**
	 * {@inheritDoc}
	 * See {@link ModelMapperService#convertListEntityAndListDto(List, Class)} for details.
	 */
	public <S, D> List<D> convertListEntityAndListDto(List<S> sourceList, Class<D> destinationType) {
		return sourceList.stream().map(source -> convertEntityAndDto(source, destinationType)).toList();
	}

}