package com.catering.service.tenant.impl;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dao.journal_voucher.JournalVoucherNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.JournalVoucherCalculationListDto;
import com.catering.dto.tenant.request.JournalVoucherDto;
import com.catering.dto.tenant.request.JournalVoucherResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.JournalVoucherDetailsModel;
import com.catering.model.tenant.JournalVoucherModel;
import com.catering.repository.tenant.JournalVoucherRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.JournalVoucherService;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for handling Journal Voucher operations.
 * This service provides methods for creating, retrieving, and managing Journal Vouchers,
 * including validation and calculation of associated transaction details.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JournalVoucherServiceImpl extends GenericServiceImpl<JournalVoucherResponseDto, JournalVoucherModel, Long> implements JournalVoucherService {

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Journal Voucher entities.
	 */
	JournalVoucherRepository journalVoucherRepository;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for executing native queries related to Journal Voucher operations.
	 */
	JournalVoucherNativeQueryService journalVoucherNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<JournalVoucherDto> createAndUpdate(JournalVoucherDto journalVoucherDto) throws RestException {
		journalVoucherDto.setIsActive(true);
		JournalVoucherModel journalVoucherModel = modelMapperService.convertEntityAndDto(journalVoucherDto, JournalVoucherModel.class);
		List<JournalVoucherDetailsModel> journalVoucherDetails = journalVoucherModel.getJournalVoucherDetails();
		validateCalculation(journalVoucherDetails);
		journalVoucherDetails.forEach(dto -> dto.setJournalVoucher(journalVoucherModel));
		DataUtils.setAuditFields(journalVoucherRepository, journalVoucherDto.getId(), journalVoucherModel);
		journalVoucherModel.setJournalVoucherDetails(journalVoucherDetails);
		return Optional.of(modelMapperService.convertEntityAndDto(journalVoucherRepository.save(journalVoucherModel), JournalVoucherDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<JournalVoucherResponseDto>> getAllJournalVoucher(FilterDto filterDto) {
		String query = filterDto.getQuery();
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if (StringUtils.isNotBlank(filterDto.getQuery()) || StringUtils.isNotBlank(filterDto.getSortBy())) {
			// Get all records of Journal Voucher
			ResponseContainerDto<List<JournalVoucherResponseDto>> allJournalVoucher = RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(journalVoucherRepository.findAll(), JournalVoucherResponseDto.class));
			setCalculationData(allJournalVoucher.getBody());
			List<JournalVoucherResponseDto> journalVoucherFilteredRecords = filterDateAndCRDRValue(allJournalVoucher.getBody(), query);
			getSortedData(filterDto.getSortBy(), journalVoucherFilteredRecords, filterDto.getSortDirection());
			// Convert to Page so we can reuse getPaging()
			int start = Math.min((Integer.parseInt(filterDto.getCurrentPage()) - 1) * (Integer.parseInt(filterDto.getSizePerPage())), journalVoucherFilteredRecords.size());
			int end = Math.min(start + (Integer.parseInt(filterDto.getSizePerPage())), journalVoucherFilteredRecords.size());

			Page<JournalVoucherResponseDto> pages = new PageImpl<>(journalVoucherFilteredRecords.subList(start, end), PageRequest.of((Integer.parseInt(filterDto.getCurrentPage()) - 1), (Integer.parseInt(filterDto.getSizePerPage()))), journalVoucherFilteredRecords.size());
			// Now use your existing paging method
			Optional<Paging> paging = PagingUtils.getPaging(pages);
			// Build the response
			ResponseContainerDto<List<JournalVoucherResponseDto>> result = RequestResponseUtils.generateResponseDto(pages.getContent());
			result.setPaging(paging.orElse(Paging.builder().build()));
			return result;
		}
		ResponseContainerDto<List<JournalVoucherResponseDto>> journalVoucherResponseDtos = read(JournalVoucherResponseDto.class, JournalVoucherModel.class, filterDto, Optional.empty());
		setCalculationData(journalVoucherResponseDtos.getBody());
		return journalVoucherResponseDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteJournalVoucher(Long id) {
		if (!journalVoucherRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		journalVoucherRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<JournalVoucherDto> getJournalVoucher(Long id) throws RestException {
		Optional<JournalVoucherModel> journalVoucherModelOp = journalVoucherRepository.findById(id);
		if (journalVoucherModelOp.isPresent()) {
			return Optional.of(modelMapperService.convertEntityAndDto(journalVoucherModelOp.get(), JournalVoucherDto.class));
		} else {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		return Optional.empty();
	}

	/**
	 * Updates the total credit (CR) and total debit (DR) fields for each JournalVoucherResponseDto
	 * in the provided list by retrieving the calculation data from the journalVoucherNativeQueryService.
	 *
	 * @param journalVoucherResponseDtos A list of JournalVoucherResponseDto objects to update
	 *									 with calculation data for their total credit and debit values.
	 * @return A list of JournalVoucherResponseDto objects with updated totalCr and totalDr fields.
	 */
	private List<JournalVoucherResponseDto> setCalculationData(List<JournalVoucherResponseDto> journalVoucherResponseDtos) {
		journalVoucherResponseDtos.forEach(journalVoucher -> {
			JournalVoucherCalculationListDto calculationListDto = journalVoucherNativeQueryService.getCalculationOfJournalVoucher(journalVoucher.getId());
			journalVoucher.setTotalCr(calculationListDto.getTotalCr());
			journalVoucher.setTotalDr(calculationListDto.getTotalDr());
		});
		return journalVoucherResponseDtos;
	}

	/**
	 * Validates the total credit (CR) and total debit (DR) amounts in the provided list
	 * of JournalVoucherDetailsModel instances. Throws a BadRequestException if the total
	 * credit and debit amounts are mismatched.
	 *
	 * @param journalVoucherDetailsModels A list of JournalVoucherDetailsModel objects
	 *									  containing transaction details to validate.
	 */
	private void validateCalculation(List<JournalVoucherDetailsModel> journalVoucherDetailsModels) {
		Double totalCR = journalVoucherDetailsModels.stream().filter(journalVoucherDetail-> journalVoucherDetail.isTransactionType()).mapToDouble(journalVoucher -> journalVoucher.getAmount()).sum();
		Double totalDR = journalVoucherDetailsModels.stream().filter(journalVoucherDetail-> !journalVoucherDetail.isTransactionType()).mapToDouble(journalVoucher -> journalVoucher.getAmount()).sum();
		if (!totalCR.equals(totalDR)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_JOURNAL_VOUCHER_DETAILS_CR_DR_AMOUNT_MISMATCH));
		}
	}

	/**
	 * Sorts a list of {@link JournalVoucherResponseDto} based on a specified field and sort direction.
	 *
	 * @param sortByField the field name used for sorting (e.g., "totalCrDr", "voucherDate")
	 * @param journalVoucherFilterData the list of journal voucher records to sort
	 * @param sortDirection the sort direction, either "asc" for ascending or "desc" for descending
	 */
	private void getSortedData(String sortByField, List<JournalVoucherResponseDto> journalVoucherFilterData, String sortDirection) {
		if (Objects.nonNull(sortByField) && StringUtils.isNotBlank(sortByField)) {
			Comparator<JournalVoucherResponseDto> comparator = null;
			// Define comparator based on the field name
			switch (sortByField) {
			case FieldConstants.TOTAL_CR_DR:
				comparator = Comparator.comparing(JournalVoucherResponseDto::getTotalCr);
				break;
			case FieldConstants.VOUCHER_DATE:
				comparator = Comparator.comparing(JournalVoucherResponseDto::getVoucherDate);
				break;
			}

			// Apply sort if comparator is set
			if (comparator != null) {
				// Reverse comparator if sort direction is descending
				if ("desc".equalsIgnoreCase(sortDirection)) {
					comparator = comparator.reversed();
				}
				// Sort the list using the comparator
				journalVoucherFilterData.sort(comparator);
			}
		}
	}

	/**
	 * Filters a list of {@link JournalVoucherResponseDto} based on a search query.
	 * The query is matched against voucher date (in multiple formats) and total credit amount.
	 *
	 * @param journalVoucherRecords the list of journal voucher records to filter
	 * @param query the search query string
	 * @return a list of {@link JournalVoucherResponseDto} that match the query criteria
	 */
	private List<JournalVoucherResponseDto> filterDateAndCRDRValue(List<JournalVoucherResponseDto> journalVoucherRecords, String query) {
		return journalVoucherRecords.stream()
				.filter(data -> 
				data.getVoucherDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getVoucherDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getVoucherDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString().toLowerCase().contains(query.toLowerCase()) || 
				data.getTotalCr().toString().toLowerCase().contains(query.toLowerCase())
			)
			.collect(Collectors.toList());
	}

}