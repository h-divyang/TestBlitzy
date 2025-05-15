package com.catering.service.tenant.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.CompanyDto;
import com.catering.model.superadmin.CompanyModel;
import com.catering.repository.tenant.CompanyRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.CompanyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CompanyService} interface that provides operations
 * for managing and retrieving company data such as existence checks and details retrieval
 * based on various unique identifiers like tenant UUID or unique code.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyServiceImpl implements CompanyService {

	/**
	 * Repository for managing Company entity interactions with the database.
	 */
	CompanyRepository companyRepository;

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByTenant(UUID tenant) {
		return companyRepository.existsByTenant(tenant);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByUniqueCode(String uniqueCode) {
		return companyRepository.existsByUniqueCode(uniqueCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyDto> findByByUniqueCode(String uniqueCode) {
		return convertCompanyModelToDto(companyRepository.findByUniqueCode(uniqueCode));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyDto> findById(Long companyId) {
		return convertCompanyModelToDto(companyRepository.findById(companyId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyDto> findByTenant(UUID uuid) {
		return convertCompanyModelToDto(companyRepository.findByTenant(uuid));
	}

	/**
	 * Converts an Optional containing a {@link CompanyModel} object into an Optional containing
	 * a {@link CompanyDto} object. If the input Optional is empty, an empty Optional is returned.
	 *
	 * @param company The Optional containing the {@link CompanyModel} to be converted.
	 * @return An Optional containing the converted {@link CompanyDto}, or an empty Optional if the input was empty.
	 */
	private Optional<CompanyDto> convertCompanyModelToDto(Optional<CompanyModel> company) {
		if (company.isPresent()) {
			return Optional.of(modelMapperService.convertEntityAndDto(company.get(), CompanyDto.class));
		}
		return Optional.empty();
	}

}