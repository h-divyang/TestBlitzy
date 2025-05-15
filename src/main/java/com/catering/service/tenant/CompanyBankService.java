package com.catering.service.tenant;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.catering.dto.tenant.request.CompanyBankDto;
import com.catering.model.tenant.CompanyBankModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing company bank details and associated QR codes.
 * This interface extends the {@link GenericService} interface, inheriting its CRUD operations,
 * and includes specific operations for handling company bank details and QR codes.
 */
public interface CompanyBankService extends GenericService<CompanyBankDto, CompanyBankModel, Long> {

	/**
	 * Creates or updates a CompanyBankDto object and associates it with a QR code.
	 *
	 * @param companyBankDto The CompanyBankDto object containing bank details to be created or updated.
	 * @param qrCode The MultipartFile object representing the QR code image file to associate with the bank details.
	 * @return An Optional containing the updated or created CompanyBankDto object, or an empty Optional if the operation fails.
	 */
	Optional<CompanyBankDto> createAndUpdate(CompanyBankDto companyBankDto, MultipartFile qrCode);

	/**
	 * Retrieves an Optional containing a CompanyBankDto object.
	 *
	 * @return An Optional containing the CompanyBankDto object if available, or an empty Optional if no data is found.
	 */
	Optional<CompanyBankDto> read();

	/**
	 * Deletes the associated QR code for the bank details.
	 *
	 * This method removes the QR code that has been assigned to a particular company's bank details.
	 */
	void deleteQrCode();

}