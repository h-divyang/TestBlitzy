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
import org.springframework.transaction.annotation.Transactional;

import com.catering.bean.ErrorGenerator;
import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dao.bank_payment_receipt.BankPaymentReceiptNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.BankPaymentReceiptDetailsDto;
import com.catering.dto.tenant.request.BankPaymentReceiptRequestDto;
import com.catering.dto.tenant.request.BankPaymentReceiptResponseDto;
import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.BankPaymentReceiptModel;
import com.catering.repository.tenant.BankPaymentReceiptRepository;
import com.catering.repository.tenant.OrderInvoiceRepository;
import com.catering.repository.tenant.OrderProformaInvoiceRepository;
import com.catering.repository.tenant.OrderQuotationRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.BankPaymentReceiptService;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing Bank Payment Receipts.
 * This class provides functionalities to manage Bank Payment Receipt operations, including creation,
 * updating, filtering, and retrieval of receipt details.
 *
 * Extends the {@code GenericServiceImpl} for base service operations.
 * Implements the `BankPaymentReceiptService` interface for specific business logic.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BankPaymentReceiptServiceImpl extends GenericServiceImpl<BankPaymentReceiptResponseDto,BankPaymentReceiptModel,Long> implements  BankPaymentReceiptService {

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for managing and delivering localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling custom exceptions and exception logic.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing Bank Payment Receipt entity interactions with the database.
	 */
	BankPaymentReceiptRepository bankPaymentReceiptRepository;

	/**
	 * Service for executing native queries related to Bank Payment Receipts.
	 */
	BankPaymentReceiptNativeQueryService bankPaymentReceiptNativeQueryService;

	/**
	 * Repository for managing Order Invoice entity interactions with the database.
	 */
	OrderInvoiceRepository orderInvoiceRepository;

	/**
	 * Repository for managing Order Quotation entity interactions with the database.
	 */
	OrderQuotationRepository orderQuotationRepository;

	/**
	 * Repository for managing Order Proforma Invoice entity interactions with the database.
	 */
	OrderProformaInvoiceRepository orderProformaInvoiceRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<BankPaymentReceiptResponseDto>> read(FilterDto filterDto, boolean voucherType) {
		String query = "";
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		query = filterDto.getQuery();
		if (StringUtils.isNotBlank(filterDto.getQuery()) || StringUtils.isNotBlank(filterDto.getSortBy())) {
			// Get all records of BankPaymentReceipt by voucher type
			ResponseContainerDto<List<BankPaymentReceiptResponseDto>> allBankPaymentReceiptRecordsByTransactionType = RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(bankPaymentReceiptRepository.findByTransactionType(voucherType), BankPaymentReceiptResponseDto.class));
			setSupplierContactsAndAmountForRecords(allBankPaymentReceiptRecordsByTransactionType.getBody());
			List<BankPaymentReceiptResponseDto> filteredReceipts = filterReceiptsBySupplierName(allBankPaymentReceiptRecordsByTransactionType.getBody(), query);
			getSortedData(filterDto.getSortBy(), filteredReceipts, filterDto.getSortDirection());
			// Convert to Page so we can reuse getPaging()
			int start = Math.min((Integer.parseInt(filterDto.getCurrentPage()) - 1) * (Integer.parseInt(filterDto.getSizePerPage())), filteredReceipts.size());
			int end = Math.min(start + (Integer.parseInt(filterDto.getSizePerPage())), filteredReceipts.size());

			Page<BankPaymentReceiptResponseDto> pages = new PageImpl<>(filteredReceipts.subList(start, end), PageRequest.of((Integer.parseInt(filterDto.getCurrentPage()) - 1), (Integer.parseInt(filterDto.getSizePerPage()))), filteredReceipts.size());
			// Now use your existing paging method
			Optional<Paging> paging = PagingUtils.getPaging(pages);

			// Build the response
			ResponseContainerDto<List<BankPaymentReceiptResponseDto>> result = RequestResponseUtils.generateResponseDto(pages.getContent());
			result.setPaging(paging.orElse(Paging.builder().build()));

			return result;
		} else {
			ResponseContainerDto<List<BankPaymentReceiptResponseDto>> bankPaymentReceiptRecords = read(BankPaymentReceiptResponseDto.class, BankPaymentReceiptModel.class, filterDto, Optional.empty());
			setSupplierContactsAndAmountForRecords(bankPaymentReceiptRecords.getBody());
			return bankPaymentReceiptRecords;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Optional<BankPaymentReceiptRequestDto> createUpdateBankPaymentReceipt(BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) throws RestException {
		bankPaymentReceiptRequestDto.setIsActive(true);
		validateFields(bankPaymentReceiptRequestDto);
		BankPaymentReceiptModel bankPaymentReceiptModel = modelMapperService.convertEntityAndDto(bankPaymentReceiptRequestDto, BankPaymentReceiptModel.class);
		bankPaymentReceiptModel.getBankPaymentReceiptDetailsList().forEach(bankPaymentReceiptDetails -> bankPaymentReceiptDetails.setBankPaymentReceipt(bankPaymentReceiptModel));
		DataUtils.setAuditFields(bankPaymentReceiptRepository, bankPaymentReceiptRequestDto.getId(), bankPaymentReceiptModel);
		BankPaymentReceiptRequestDto oldData = null;
		// On edit bank receipt data get old data and subtract the amount in order invoice for that voucher number
		if (Objects.nonNull(bankPaymentReceiptRequestDto.getId()) && bankPaymentReceiptRequestDto.isTransactionType()) {
			oldData = modelMapperService.convertEntityAndDto(bankPaymentReceiptRepository.findById(bankPaymentReceiptRequestDto.getId()).get(), BankPaymentReceiptRequestDto.class);
			if (oldData != null) {
				oldData.getBankPaymentReceiptDetailsList().forEach(data -> {
					if (data.getVoucherType() == 6 && data.getVoucherNumber() != null) {
						orderInvoiceRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
					}
					if (data.getVoucherType() == 7 && data.getVoucherNumber() != null) {
						if (orderInvoiceRepository.existsByOrderId(data.getVoucherNumber())) {
							orderInvoiceRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
						}
						if (orderQuotationRepository.existsByOrderId(data.getVoucherNumber())) {
							orderQuotationRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
						}
						if (orderProformaInvoiceRepository.existsByOrderId(data.getVoucherNumber())) {
							orderProformaInvoiceRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
						}
					}
				});
			}
		}
		BankPaymentReceiptRequestDto bankDto = modelMapperService.convertEntityAndDto(bankPaymentReceiptRepository.save(bankPaymentReceiptModel), BankPaymentReceiptRequestDto.class);
		// On add/edit bank receipt add amount in order invoice for that voucher number
		if (bankPaymentReceiptRequestDto.isTransactionType()) {
			for (BankPaymentReceiptDetailsDto data: bankPaymentReceiptRequestDto.getBankPaymentReceiptDetailsList()) {
				if (data.getVoucherType() == 6 && data.getVoucherNumber() != null) {
					orderInvoiceRepository.updateAdvancePayment(0, data.getAmount(), data.getVoucherNumber());
				}
				if (data.getVoucherType() == 7 && data.getVoucherNumber() != null) {
					if (orderInvoiceRepository.existsByOrderId(data.getVoucherNumber())) {
						orderInvoiceRepository.updateAdvancePayment(0, data.getAmount(), data.getVoucherNumber());
					}
					if (orderQuotationRepository.existsByOrderId(data.getVoucherNumber())) {
						orderQuotationRepository.updateAdvancePayment(0, data.getAmount(), data.getVoucherNumber());
					}
					if (orderProformaInvoiceRepository.existsByOrderId(data.getVoucherNumber())) {
						orderProformaInvoiceRepository.updateAdvancePayment(0, data.getAmount(), data.getVoucherNumber());
					}
				}
			}
		}
		return Optional.of(bankDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<BankPaymentReceiptRequestDto> getBankPaymentReceipt(Long id) throws RestException {
		Optional<BankPaymentReceiptModel> bankPaymentReceiptOp = bankPaymentReceiptRepository.findById(id);
		if (bankPaymentReceiptOp.isPresent()) {
			return Optional.of(modelMapperService.convertEntityAndDto(bankPaymentReceiptOp.get(), BankPaymentReceiptRequestDto.class));
		} else {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteBankPaymentReceipt(Long id) {
		if (!bankPaymentReceiptRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<BankPaymentReceiptModel> bankModel = bankPaymentReceiptRepository.findById(id);
		// On delete any bank receipt data update its amount in order invoice
		if (bankModel.get().isTransactionType()) {
			bankModel.get().getBankPaymentReceiptDetailsList().forEach(data -> { 
				if (data.getVoucherType() == 6) {
					orderInvoiceRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
				}
				if (data.getVoucherType() == 7 && data.getVoucherNumber() != null) {
					if (orderInvoiceRepository.existsByOrderId(data.getVoucherNumber())) {
						orderInvoiceRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
					}
					if (orderQuotationRepository.existsByOrderId(data.getVoucherNumber())) {
						orderQuotationRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
					}
					if (orderProformaInvoiceRepository.existsByOrderId(data.getVoucherNumber())) {
						orderProformaInvoiceRepository.updateAdvancePayment(1, data.getAmount(), data.getVoucherNumber());
					}
				}
			});
		}
		bankPaymentReceiptRepository.deleteById(id);
	}

	/**
	 * Validates the required fields in the provided BankPaymentReceiptRequestDto and generates errors if any field is invalid.
	 *
	 * @param bankPaymentReceiptRequestDto The BankPaymentReceiptRequestDto object containing the fields to be validated.
	 */
	private void validateFields(BankPaymentReceiptRequestDto bankPaymentReceiptRequestDto) {
		ErrorGenerator error = ErrorGenerator.builder();
		if (bankPaymentReceiptRequestDto.getTransactionDate() == null) {
			error.putError(FieldConstants.CASH_PAYMENT_RECEIPT_TRANSACTION_DATE, messageService.getMessage(MessagesConstant.VALIDATION_CASH_PAYMENT_RECEIPT_TRANSACTION_DATE));
		}
		if (bankPaymentReceiptRequestDto.getBankPaymentReceiptDetailsList().isEmpty()) {
			error.putError(FieldConstants.CASH_PAYMENT_RECEIPT_DETAILS_LIST, messageService.getMessage(MessagesConstant.VALIDATION_CASH_PAYMENT_RECEIPT_DETAILS_LIST));
		}
		if (error.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), error.getErrors());
		}
	}


	/**
	 * Updates the supplier contact details and total amount for each record in the given list of bank payment receipt records.
	 * This involves fetching the supplier contact information for each record using their respective IDs
	 * and setting these details along with the total amount to the corresponding DTO fields.
	 *
	 * @param bankPaymentReceiptRecords A list of {@code BankPaymentReceiptResponseDto} objects representing bank payment receipt records.
	 *									Each record will be updated with supplier contact details and total amount if applicable.
	 */
	private void setSupplierContactsAndAmountForRecords(List<BankPaymentReceiptResponseDto> bankPaymentReceiptRecords) {
		bankPaymentReceiptRecords.forEach(data -> {
			CashBankPaymentReceiptCommonResultListDto contactList = bankPaymentReceiptNativeQueryService.getSuppilerContact(data.getId());
			if (contactList.getNameDefaultLang() != null) {
				ContactResponseDto contact = new ContactResponseDto();
				contact.setNameDefaultLang(contactList.getNameDefaultLang());
				contact.setNamePreferLang(contactList.getNamePreferLang());
				contact.setNameSupportiveLang(contactList.getNameSupportiveLang());
				data.setContactSupplier(contact);
			}
			data.setTotalAmount(contactList.getTotalAmount());
		});
	}

	/**
	 * Filters a list of bank payment receipt DTOs based on the supplier's name or the transaction date.
	 * This method checks if the supplier's name in any of the available languages or the transaction
	 * date (formatted in different formats) contains the specified query, and returns a filtered list.
	 *
	 * @param receipts A list of {@code BankPaymentReceiptResponseDto} objects representing the bank payment receipts.
	 *				   Each object contains details such as the supplier contact and transaction date.
	 * @param query A string query used to search in the supplier's name or transaction date.
	 * @return A filtered list of {@code BankPaymentReceiptResponseDto} objects where the supplier's name
	 *		   or transaction date matches the query.
	 */
	private List<BankPaymentReceiptResponseDto> filterReceiptsBySupplierName(List<BankPaymentReceiptResponseDto> receipts, String query) {
		return receipts.stream()
				.filter(data ->
				(data.getContactSupplier() != null && (
						(data.getContactSupplier().getNameDefaultLang() != null && data.getContactSupplier().getNameDefaultLang().toLowerCase().contains(query.toLowerCase())) ||
						(data.getContactSupplier().getNamePreferLang() != null && data.getContactSupplier().getNamePreferLang().toLowerCase().contains(query.toLowerCase())) ||
						(data.getContactSupplier().getNameSupportiveLang() != null && data.getContactSupplier().getNameSupportiveLang().toLowerCase().contains(query.toLowerCase()))
				)) ||
				data.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getTransactionDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString().toLowerCase().contains(query.toLowerCase())
			)
			.collect(Collectors.toList());
	}

	/**
	 * Sorts a list of BankPaymentReceiptResponseDto objects based on the specified field and direction.
	 *
	 * @param sortByField     the field by which to sort (supports nested contact fields and basic fields)
	 * @param filteredReceipts the list of receipts to sort
	 * @param sortDirection   the direction of sorting ("asc" or "desc")
	 */
	private void getSortedData(String sortByField, List<BankPaymentReceiptResponseDto> filteredReceipts, String sortDirection) {
		if (Objects.nonNull(sortByField) && StringUtils.isNotBlank(sortByField)) {
			Comparator<BankPaymentReceiptResponseDto> comparator = null;
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
			case FieldConstants.TOTAL_AMOUNT:
				comparator = Comparator.comparing(BankPaymentReceiptResponseDto::getTotalAmount);
				break;
			case FieldConstants.TRANSACTION_DATE:
				comparator = Comparator.comparing(BankPaymentReceiptResponseDto::getTransactionDate);
				break;
			}

			// Apply sort if comparator is set
			if (comparator != null) {
				// Reverse comparator if sort direction is descending
				if ("desc".equalsIgnoreCase(sortDirection)) {
					comparator = comparator.reversed();
				}
				// Sort the list using the comparator
				filteredReceipts.sort(comparator);
			}
		}
	}

}