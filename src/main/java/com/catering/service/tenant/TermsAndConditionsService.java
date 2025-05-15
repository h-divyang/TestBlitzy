package com.catering.service.tenant;

import java.util.Optional;
import javax.validation.Valid;
import com.catering.dto.tenant.request.TermsAndConditionsDto;
import com.catering.model.tenant.TermsAndConditionsModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing terms and conditions data.
 *
 * @param <TermsAndConditionsDto>   Data Transfer Object (DTO) representing terms and conditions.
 * @param <TermsAndConditionsModel> Entity model class representing terms and conditions.
 * @param <Long>                    Type of the entity's primary key.
 *
 * @since 2023-12-18
 * @author Krushali Talaviya
 */
public interface TermsAndConditionsService extends GenericService<TermsAndConditionsDto, TermsAndConditionsModel, Long> {

	/**
	 * Retrieves terms and conditions data by its identifier.
	 *
	 * @return TermsAndConditionsDto representing the retrieved data.
	 */
	public TermsAndConditionsDto getTermsAndConditionsData();

	/**
	 * Saves terms and conditions data.
	 *
	 * @param termsAndConditionsDto The data to be saved.
	 * @return Optional containing the saved terms and conditions data.
	 *
	 * @throws ValidationException if the input data is not valid.
	 */
	public Optional<TermsAndConditionsDto> saveTermsAndConitionsData(@Valid TermsAndConditionsDto termsAndConditionsDto);

	/**
	 * Retrieves the Terms and Conditions report content in byte array format for a specified language type.
	 *
	 * @param langType The language type for the report.
	 * @return A byte array representing the Terms and Conditions report in PDF format.
	 *         If the report content is not available or an error occurs during conversion, an empty byte array is returned.
	 *
	 * @param langType The language type for the report.
	 * @return A byte array representing the Terms and Conditions report in PDF format.
	 *         If the report content is not available or an error occurs during conversion, an empty byte array is returned.
	 */
	public byte[] getTermsAndConditionsInByte(Integer langType);

}