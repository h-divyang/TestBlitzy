package com.catering.service.tenant.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.Constants;
import com.catering.dto.tenant.request.CompanyBankDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.CompanyBankModel;
import com.catering.repository.tenant.CompanyBankRepository;
import com.catering.service.common.FileService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.S3Service;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanyBankService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CompanyBankService} interface.
 * This class provides functionality for managing company bank details as well as the associated QR codes,
 * building upon the generic operations defined in {@link GenericServiceImpl}.
 * It uses various services such as {@link ModelMapperService}, {@link CompanyBankRepository},
 * {@link FileService}, and {@link S3Service} to handle mapping, file operations, and database persistence.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyBankServiceImpl extends GenericServiceImpl<CompanyBankDto, CompanyBankModel, Long> implements CompanyBankService {

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Company Bank entity interactions with the database.
	 */
	CompanyBankRepository companyBankRepository;

	/**
	 * Service for handling file-related operations such as upload, download, and management.
	 */
	FileService fileService;

	/**
	 * Service for integrating with Amazon S3 for file storage and retrieval operations.
	 */
	S3Service s3Service;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyBankDto> createAndUpdate(CompanyBankDto companyBankDto, MultipartFile qrCode) throws RestException {
		if (Objects.nonNull(qrCode)) {
			fileService.upload(qrCode, fileService.createKey(Constants.QRCODE));
		}
		CompanyBankModel companyBankModel = modelMapperService.convertEntityAndDto(companyBankDto, CompanyBankModel.class);
		DataUtils.setAuditFields(companyBankRepository, companyBankDto.getId(), companyBankModel);
		return Optional.of(modelMapperService.convertEntityAndDto(companyBankRepository.save(companyBankModel), CompanyBankDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyBankDto> read() {
		Optional<CompanyBankModel> companyBankModelOptional = companyBankRepository.findFirstByOrderByIdAsc();
		if (companyBankModelOptional.isPresent()) {
			CompanyBankModel companyBankModel = companyBankModelOptional.get();
			CompanyBankDto companyBankDto = modelMapperService.convertEntityAndDto(companyBankModel, CompanyBankDto.class);
			companyBankDto.setQrCode(fileService.getUrl(Constants.QRCODE));
			return Optional.of(companyBankDto);
		}
		return Optional.empty(); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQrCode() {
		s3Service.delete(fileService.createKey(Constants.QRCODE));
	}

}