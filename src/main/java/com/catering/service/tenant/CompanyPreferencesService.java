package com.catering.service.tenant;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.CompanyPreferencesRegistrationDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.CompanyPreferencesModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing company preferences. This interface provides functionality
 * for retrieving, updating, saving, and checking the active status of company preferences.
 * It extends the GenericService interface to inherit common CRUD operations.
 */
public interface CompanyPreferencesService extends GenericService<CompanyPreferencesDto, CompanyPreferencesModel, Long> {

	/**
	 * Finds and retrieves the company preferences information.
	 *
	 * @return An {@code Optional} containing the {@code CompanyPreferencesDto} object.
	 */
	Optional<CompanyPreferencesDto> find();

	/**
	 * Updates the company preferences information with the provided details.
	 *
	 * @param companyPreferencesDto The data transfer object containing the updated company preferences.
	 * @param logo The multipart file representing the company's logo.
	 * @param request The HTTP servlet request containing request-specific information.
	 * @return An {@code Optional} containing the updated {@code CompanyPreferencesDto}, if the update is successful.
	 * @throws RestException If an error occurs during the update process.
	 */
	Optional<CompanyPreferencesDto> update(CompanyPreferencesDto companyPreferencesDto, MultipartFile logo, HttpServletRequest request) throws RestException;

	/**
	 * Saves the company preferences and user registration details.
	 *
	 * @param companyPreferencesDto The data transfer object containing the company preferences registration details.
	 * @param companyUserDto The data transfer object containing the company user registration details.
	 */
	void save(CompanyPreferencesRegistrationDto companyPreferencesDto, CompanyUserRegistrationDto companyUserDto);

	/**
	 * Determines whether a specific entity or configuration is active.
	 *
	 * @return A {@code Boolean} indicating the active status. Returns {@code true} if active, otherwise {@code false}.
	 */
	Boolean isActive();

}