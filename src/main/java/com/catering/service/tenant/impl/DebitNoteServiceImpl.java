package com.catering.service.tenant.impl;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dao.debit_note.DebitNoteNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.DebitNoteGetByIdDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRequestDto;
import com.catering.dto.tenant.request.DebitNoteResponseDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.enums.VoucherTypeEnum;
import com.catering.model.tenant.DebitNoteModel;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.DebitNoteRepository;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CashPaymentReceiptService;
import com.catering.service.tenant.DebitNoteService;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DebitNoteServiceImpl handles all the business logic related to Debit Notes.
 * It extends the generic service implementation and provides concrete implementations
 * for managing debit notes, including creating, reading, updating, deleting,
 * and performing specific calculations associated with debit notes.
 *
 * The service interacts with various repositories and utility services to perform
 * its operations, ensuring proper validation, mapping, and data integrity checks.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DebitNoteServiceImpl extends GenericServiceImpl<DebitNoteResponseDto, DebitNoteModel, Long> implements DebitNoteService {

	/**
	 * Service for mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Debit Note entities.
	 */
	DebitNoteRepository debitNoteRepository;

	/**
	 * Service for handling native queries related to Debit Notes.
	 */
	DebitNoteNativeQueryService debitNoteNativeQueryService;

	/**
	 * Service for managing and retrieving localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling and throwing application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing Tax Master entities.
	 */
	TaxMasterRepository taxMasterRepository;

	/**
	 * Repository for managing Contact entities.
	 */
	ContactRepository contactRepository;

	/**
	 * Service for handling Cash Payment Receipts operations.
	 */
	CashPaymentReceiptService cashPaymentReceiptService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<DebitNoteRequestDto> createAndUpdate(DebitNoteRequestDto debitNoteRequestDto) {
		DebitNoteModel debitNoteModel = modelMapperService.convertEntityAndDto(debitNoteRequestDto, DebitNoteModel.class);
		validateCalculationFields(debitNoteModel);
		if (Objects.nonNull(debitNoteModel.getDebitNoteRawMaterialList())) {
			debitNoteModel.getDebitNoteRawMaterialList().forEach(rawMaterial -> rawMaterial.setDebitNote(debitNoteModel));
		}
		DataUtils.setAuditFields(debitNoteRepository, debitNoteRequestDto.getId(), debitNoteModel);
		DebitNoteRequestDto debitNoteResponseDto = modelMapperService.convertEntityAndDto(debitNoteRepository.save(debitNoteModel), DebitNoteRequestDto.class);
		if (Objects.nonNull(debitNoteRequestDto.getId())) {
			debitNoteRepository.calculateAccountBalance(1l, debitNoteResponseDto.getId());
		} else {
			debitNoteRepository.calculateAccountBalance(0l, debitNoteResponseDto.getId());
		}
		return Optional.of(debitNoteResponseDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<DebitNoteResponseDto>> read(FilterDto filterDto) {
		String query = filterDto.getQuery();
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if (StringUtils.isNotBlank(filterDto.getQuery()) || StringUtils.isNotBlank(filterDto.getSortBy())) {
			// Get all records of debit note
			ResponseContainerDto<List<DebitNoteResponseDto>> allDebitNote = RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(debitNoteRepository.findAll(), DebitNoteResponseDto.class));
			getCalculation(allDebitNote.getBody());
			List<DebitNoteResponseDto> allDebitNoteFilteredRecord = filterDateContactAmount(allDebitNote.getBody(), query);
			getSortedData(filterDto.getSortBy(), allDebitNoteFilteredRecord, filterDto.getSortDirection());
			// Convert to Page so we can reuse getPaging()
			int start = Math.min((Integer.parseInt(filterDto.getCurrentPage()) - 1) * (Integer.parseInt(filterDto.getSizePerPage())), allDebitNoteFilteredRecord.size());
			int end = Math.min(start + (Integer.parseInt(filterDto.getSizePerPage())), allDebitNoteFilteredRecord.size());

			Page<DebitNoteResponseDto> pages = new PageImpl<>(allDebitNoteFilteredRecord.subList(start, end), PageRequest.of((Integer.parseInt(filterDto.getCurrentPage()) - 1), (Integer.parseInt(filterDto.getSizePerPage()))), allDebitNoteFilteredRecord.size());
			// Now use your existing paging method
			Optional<Paging> paging = PagingUtils.getPaging(pages);
			// Build the response
			ResponseContainerDto<List<DebitNoteResponseDto>> result = RequestResponseUtils.generateResponseDto(pages.getContent());
			result.setPaging(paging.orElse(Paging.builder().build()));
			return result;
		}
		ResponseContainerDto<List<DebitNoteResponseDto>> debitNoteResponseDto = read(DebitNoteResponseDto.class, DebitNoteModel.class, filterDto, Optional.empty());
		getCalculation(debitNoteResponseDto.getBody());
		return debitNoteResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DebitNoteGetByIdDto getById(Long id) {
		Optional<DebitNoteModel> debitNoteModel = debitNoteRepository.findById(id);
		CommonCalculationFieldDto debitNoteCalculationFieldDto = debitNoteNativeQueryService.getDebitNoteListCalculation(id);
		DebitNoteGetByIdDto debitNoteGetByIdDto = modelMapperService.convertEntityAndDto(debitNoteModel.isPresent() ? debitNoteModel.get() : new DebitNoteModel(), DebitNoteGetByIdDto.class);
		debitNoteGetByIdDto.setTotalAmount(debitNoteCalculationFieldDto.getTotalAmount());
		return debitNoteGetByIdDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		Integer count = cashPaymentReceiptService.isUseInCashOrBank(id, VoucherTypeEnum.getIds(VoucherTypeEnum.DEBIT_NOTE));
		if (Objects.nonNull(count) && count > 0) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		if (!debitNoteRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			debitNoteRepository.deleteById(id);
			debitNoteRepository.calculateAccountBalance(2L, id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<DebitNotePurchaseBillDropDownDto>> getDebitNotePurchaseBillDropDownData(Long id) {
		return RequestResponseUtils.generateResponseDto(debitNoteNativeQueryService.getDebitNotePurchaseBillDropDown(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseBillOrderRawMaterialDto>> getDebitNotePurchaseBillRawMaterial(Long id) {
		return RequestResponseUtils.generateResponseDto(debitNoteNativeQueryService.getDebitNotePurchaseBillRawMaterial(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<DebitNoteRawMaterialDropDownDto>> getRawMaterialDropDownData() {
		return RequestResponseUtils.generateResponseDto(debitNoteNativeQueryService.getDebitNoteRawMaterial());
	}

	/**
	 * Validates and calculates the fields for the debit note model, specifically for its raw material list.
	 * Adjusts the price and total amount based on the presence of tax master details and GST number validation.
	 *
	 * @param debitNoteModel The DebitNoteModel object containing the list of raw materials and associated data for validation and calculations.
	 */
	private void validateCalculationFields(DebitNoteModel debitNoteModel) {
		List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
		boolean gstSameOrNot = contactRepository.isGstNumberSame(debitNoteModel.getContactSupplier().getId());
		if (Objects.nonNull(debitNoteModel.getDebitNoteRawMaterialList())) {
			debitNoteModel.getDebitNoteRawMaterialList().forEach(rawMaterial -> {
				if (Objects.isNull(rawMaterial.getPrice())) {
					rawMaterial.setPrice(0D);
				}
				if (Objects.nonNull(rawMaterial.getTaxMaster())) {
					Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), rawMaterial.getTaxMaster().getId())).findAny();
					Double totalAmount;
					if (gstSameOrNot) {
						totalAmount = ((rawMaterial.getWeight() * rawMaterial.getPrice() * taxMaster.get().getIgst()) / 100) + (rawMaterial.getPrice() * rawMaterial.getWeight());
					} else {
						totalAmount = ((rawMaterial.getWeight() * rawMaterial.getPrice() * taxMaster.get().getCgst()) / 100) + ((rawMaterial.getWeight() * rawMaterial.getPrice() * taxMaster.get().getSgst()) / 100) + (rawMaterial.getPrice() * rawMaterial.getWeight());
					}
					rawMaterial.setTotalAmount(totalAmount);
				} else {
					rawMaterial.setTotalAmount(rawMaterial.getWeight() * rawMaterial.getPrice());
				}
			});
		}
	}

	/**
	 * Computes and updates the calculation fields (amount, taxable amount, total amount)
	 * for each entry in the provided list of DebitNoteResponseDto objects using data
	 * retrieved from the debitNoteNativeQueryService.
	 *
	 * @param debitNoteResponseDtoList A list of DebitNoteResponseDto objects that require
	 *								   calculation updates for amount, taxableAmount, and totalAmount.
	 * @return A list of DebitNoteResponseDto objects with their calculation fields updated.
	 */
	private List<DebitNoteResponseDto> getCalculation(List<DebitNoteResponseDto> debitNoteResponseDtoList) {
		debitNoteResponseDtoList.forEach(responseDto -> {
			CommonCalculationFieldDto debitNoteCalculationFieldDto = debitNoteNativeQueryService.getDebitNoteListCalculation(responseDto.getId());
			responseDto.setAmount(debitNoteCalculationFieldDto.getAmount());
			responseDto.setTaxableAmount(debitNoteCalculationFieldDto.getTaxableAmount());
			responseDto.setTotalAmount(debitNoteCalculationFieldDto.getTotalAmount());
		});
		return debitNoteResponseDtoList;
	}

	/**
	 * Sorts the given list of {@link DebitNoteResponseDto} based on the specified field and sort direction.
	 *
	 * @param sortByField the field name to sort by (e.g., "contact.nameDefaultLang", "totalAmount")
	 * @param debitNoteFilterdata the list of debit note data to be sorted
	 * @param sortDirection the direction of sorting: "asc" for ascending or "desc" for descending
	 */
	private void getSortedData(String sortByField, List<DebitNoteResponseDto> debitNoteFilterdata, String sortDirection) {
		if (Objects.nonNull(sortByField) && StringUtils.isNotBlank(sortByField)) {
			Comparator<DebitNoteResponseDto> comparator = null;
			// Define comparator based on the field name
			switch (sortByField) {
			case FieldConstants.CONTACT + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG:
				comparator = Comparator.comparing(r -> r.getContactSupplier() != null ? r.getContactSupplier().getNameDefaultLang() : "", String.CASE_INSENSITIVE_ORDER);
				break;
			case FieldConstants.CONTACT + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG:
				comparator = Comparator.comparing(r -> r.getContactSupplier() != null ? r.getContactSupplier().getNamePreferLang() : "", String.CASE_INSENSITIVE_ORDER);
				break;
			case FieldConstants.CONTACT + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG:
				comparator = Comparator.comparing(r -> r.getContactSupplier() != null ? r.getContactSupplier().getNameSupportiveLang() : "", String.CASE_INSENSITIVE_ORDER);
				break;
			case FieldConstants.PURCHASE_BILL_NUMBER:
				comparator = Comparator.comparing(DebitNoteResponseDto::getBillNumber, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(DebitNoteResponseDto::getId, Comparator.reverseOrder());
				break;
			case FieldConstants.TOTAL_AMOUNT:
				comparator = Comparator.comparing(DebitNoteResponseDto::getTotalAmount);
				break;
			case FieldConstants.TAXABLE_AMOUNT:
				comparator = Comparator.comparing(DebitNoteResponseDto::getTaxableAmount);
				break;
			case FieldConstants.AMOUNT:
				comparator = Comparator.comparing(DebitNoteResponseDto::getAmount);
				break;
			case FieldConstants.PURCHASE_BILL_DATE:
				comparator = Comparator.comparing(DebitNoteResponseDto::getBillDate);
				break;
			}

			// Apply sort if comparator is set
			if (comparator != null) {
				// Reverse comparator if sort direction is descending
				if ("desc".equalsIgnoreCase(sortDirection)) {
					comparator = comparator.reversed();
				}
				// Sort the list using the comparator
				debitNoteFilterdata.sort(comparator);
			}
		}
	}

	/**
	 * Filters the list of {@link DebitNoteResponseDto} records by checking if the given query
	 * matches the contact supplier's name (in any language), bill date (in multiple formats),
	 * bill number, or amount fields.
	 *
	 * @param debitNoteRecords the list of debit note records to be filtered
	 * @param query the search query string used to filter the records
	 * @return a list of filtered {@link DebitNoteResponseDto} that match the query
	 */
	private List<DebitNoteResponseDto> filterDateContactAmount(List<DebitNoteResponseDto> debitNoteRecords, String query) {
		return debitNoteRecords.stream().filter(data ->
		(data.getContactSupplier() != null && (
				(data.getContactSupplier().getNameDefaultLang() != null && data.getContactSupplier().getNameDefaultLang().toLowerCase().contains(query.toLowerCase())) ||
				(data.getContactSupplier().getNamePreferLang() != null && data.getContactSupplier().getNamePreferLang().toLowerCase().contains(query.toLowerCase())) ||
				(data.getContactSupplier().getNameSupportiveLang() != null && data.getContactSupplier().getNameSupportiveLang().toLowerCase().contains(query.toLowerCase()))
			)) ||
			data.getBillDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getBillDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getBillDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getBillNumber() != null && data.getBillNumber().toLowerCase().contains(query.toLowerCase()) ||
			data.getAmount().toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getTaxableAmount().toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getTotalAmount().toString().toLowerCase().contains(query.toLowerCase())
		)
		.collect(Collectors.toList());
	}

}