package com.catering.service.tenant.impl;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
import com.catering.dao.cash_payment_receipt.CashPaymentReceiptNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.dto.tenant.request.CashPaymentReceiptDetailsDto;
import com.catering.dto.tenant.request.CashPaymentReceiptRequestDto;
import com.catering.dto.tenant.request.CashPaymentReceiptResponseDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;
import com.catering.enums.OrderTypeEnum;
import com.catering.enums.VoucherTypeEnum;
import com.catering.exception.RestException;
import com.catering.model.tenant.CashPaymentReceiptModel;
import com.catering.repository.tenant.CashPaymentReceiptRepository;
import com.catering.repository.tenant.OrderInvoiceRepository;
import com.catering.repository.tenant.OrderProformaInvoiceRepository;
import com.catering.repository.tenant.OrderQuotationRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CashPaymentReceiptService;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@code CashPaymentReceiptService} interface that provides services for managing cash
 * payment receipts, including creation, update, deletion, validation, and retrieval operations. This class
 * extends {@code GenericServiceImpl} and adheres to the tenant-based service requirements.
 *
 * The service interacts with various repositories and utilities for handling cash payment receipts, including
 * but not limited to working with invoices, quotations, and proforma invoices. Additionally, it provides
 * functionalities for filtering, querying, and validating cash payment receipts.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CashPaymentReceiptServiceImpl extends GenericServiceImpl<CashPaymentReceiptResponseDto,CashPaymentReceiptModel,Long> implements  CashPaymentReceiptService {

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
	 * Repository for managing Cash Payment Receipt entity interactions with the database.
	 */
	CashPaymentReceiptRepository cashPaymentReceiptRepository;

	/**
	 * Service for executing native queries related to Cash Payment Receipts.
	 */
	CashPaymentReceiptNativeQueryService cashPaymentReceiptNativeQueryService;

	/**
	 * Repository for managing Order Invoice entity interactions with the database.
	 */
	OrderInvoiceRepository orderInvoiceRepository;

	/**
	 * Repository for managing Order Quotation data interactions within the database.
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
	public ResponseContainerDto<List<CashPaymentReceiptResponseDto>> read(FilterDto filterDto, boolean voucherType) {
		String query = "";
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		query = filterDto.getQuery();
		if (StringUtils.isNotBlank(filterDto.getQuery()) || StringUtils.isNotBlank(filterDto.getSortBy())) {
			// Get all records of CashPaymentReceipt by voucher type
			ResponseContainerDto<List<CashPaymentReceiptResponseDto>> allCashPaymentReceiptRecordsByTransactionType = RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(cashPaymentReceiptRepository.findByTransactionType(voucherType), CashPaymentReceiptResponseDto.class));
			setSupplierContactsAndAmountForRecords(allCashPaymentReceiptRecordsByTransactionType.getBody());
			List<CashPaymentReceiptResponseDto> filteredReceipts = filterReceiptsBySupplierName(allCashPaymentReceiptRecordsByTransactionType.getBody(), query);
			getSortedData(filterDto.getSortBy(), filteredReceipts, filterDto.getSortDirection());

			// Convert to Page so we can reuse getPaging()
			int start = Math.min((Integer.parseInt(filterDto.getCurrentPage()) - 1) * (Integer.parseInt(filterDto.getSizePerPage())), filteredReceipts.size());
			int end = Math.min(start + (Integer.parseInt(filterDto.getSizePerPage())), filteredReceipts.size());

			Page<CashPaymentReceiptResponseDto> pages = new PageImpl<>(filteredReceipts.subList(start, end), PageRequest.of((Integer.parseInt(filterDto.getCurrentPage()) - 1), (Integer.parseInt(filterDto.getSizePerPage()))), filteredReceipts.size());
			// Now use your existing paging method
			Optional<Paging> paging = PagingUtils.getPaging(pages);
			// Build the response
			ResponseContainerDto<List<CashPaymentReceiptResponseDto>> result = RequestResponseUtils.generateResponseDto(pages.getContent());
			result.setPaging(paging.orElse(Paging.builder().build()));

			return result;
		} else {
			ResponseContainerDto<List<CashPaymentReceiptResponseDto>> cashPaymentReceiptRecords = read(CashPaymentReceiptResponseDto.class, CashPaymentReceiptModel.class, filterDto, Optional.empty());
			setSupplierContactsAndAmountForRecords(cashPaymentReceiptRecords.getBody());
			return cashPaymentReceiptRecords;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Optional<CashPaymentReceiptRequestDto> createUpdateCashPaymentReceipt(CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) throws RestException {
		cashPaymentReceiptRequestDto.setIsActive(true);
		validateFields(cashPaymentReceiptRequestDto);
		CashPaymentReceiptModel cashPaymentReceiptModel = modelMapperService.convertEntityAndDto(cashPaymentReceiptRequestDto, CashPaymentReceiptModel.class);
		cashPaymentReceiptModel.getCashPaymentReceiptDetailsList().forEach(cashPaymentReceiptDetails -> cashPaymentReceiptDetails.setCashPaymentReceipt(cashPaymentReceiptModel));
		DataUtils.setAuditFields(cashPaymentReceiptRepository, cashPaymentReceiptRequestDto.getId(), cashPaymentReceiptModel);
		CashPaymentReceiptRequestDto oldData = null;
		// On edit cash receipt data get old data and subtract the amount in order invoice for that voucher number
		if (Objects.nonNull(cashPaymentReceiptRequestDto.getId()) && cashPaymentReceiptRequestDto.isTransactionType()) {
			oldData = modelMapperService.convertEntityAndDto(cashPaymentReceiptRepository.findById(cashPaymentReceiptRequestDto.getId()).get(), CashPaymentReceiptRequestDto.class);
			if (oldData != null) {
				oldData.getCashPaymentReceiptDetailsList().forEach(data -> {
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
		CashPaymentReceiptRequestDto dto = modelMapperService.convertEntityAndDto(cashPaymentReceiptRepository.save(cashPaymentReceiptModel), CashPaymentReceiptRequestDto.class);
		// On add/edit cash receipt add amount in order invoice for that voucher number
		if (cashPaymentReceiptRequestDto.isTransactionType()) {
			for (CashPaymentReceiptDetailsDto data: cashPaymentReceiptRequestDto.getCashPaymentReceiptDetailsList()) {
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
		return Optional.of(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CashPaymentReceiptRequestDto> getCashPaymentReceipt(Long id) throws RestException {
		Optional<CashPaymentReceiptModel> cashPaymentReceiptOp = cashPaymentReceiptRepository.findById(id);
		if (cashPaymentReceiptOp.isPresent()) {
			return Optional.of(modelMapperService.convertEntityAndDto(cashPaymentReceiptOp.get(), CashPaymentReceiptRequestDto.class));
		} else {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCashPaymentReceipt(Long id) {
		if (!cashPaymentReceiptRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<CashPaymentReceiptModel> cashModel = cashPaymentReceiptRepository.findById(id);
		// On delete any cash receipt data update its amount in order invoice
		if (cashModel.get().isTransactionType()) {
			cashModel.get().getCashPaymentReceiptDetailsList().forEach(data -> {
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
		cashPaymentReceiptRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> getVoucherNumber(int voucherType) {
		if (voucherType == VoucherTypeEnum.LABOUR_ALLOCATION.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfLabourAllocation();
		} else if (voucherType == VoucherTypeEnum.OUTSIDE_LABOUR.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfOrderByOrderType(OrderTypeEnum.OUTSIDE_FOOD.getValue(), VoucherTypeEnum.OUTSIDE_LABOUR.getId());
		} else if (voucherType == VoucherTypeEnum.CHEF_LABOUR.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfOrderByOrderType(OrderTypeEnum.CHEF_LABOUR.getValue(), VoucherTypeEnum.CHEF_LABOUR.getId());
		} else if (voucherType == VoucherTypeEnum.PURCHASE_BILL.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfPurchaseBill();
		} else if (voucherType == VoucherTypeEnum.DEBIT_NOTE.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfDebitNote();
		} else if (voucherType == VoucherTypeEnum.INVOICE.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfInvoice();
		} else if (voucherType == VoucherTypeEnum.ADVANCE_PAYMENT.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchVoucherNumberOfAdvancePayment();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchVoucherTypeContactList(int voucherType) {
		if (voucherType == VoucherTypeEnum.LABOUR_ALLOCATION.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchContactOfLabourAllocation();
		} else if (voucherType == VoucherTypeEnum.OUTSIDE_LABOUR.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchContactOfOrderByOrderType(OrderTypeEnum.OUTSIDE_FOOD.getValue());
		} else if (voucherType == VoucherTypeEnum.CHEF_LABOUR.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchContactOfOrderByOrderType(OrderTypeEnum.CHEF_LABOUR.getValue());
		} else if (voucherType == VoucherTypeEnum.PURCHASE_BILL.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchContactOfPurchaseBill();
		} else if (voucherType == VoucherTypeEnum.INVOICE.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchContactOfInvoice();
		} else if (voucherType == VoucherTypeEnum.DEBIT_NOTE.getId()) {
			return cashPaymentReceiptNativeQueryService.fetchContactOfDebitNote();
		} else {
			return cashPaymentReceiptNativeQueryService.fetchPaymentContactList();
		}
	}

	/**
	 * Determines if a given ID and list of voucher type IDs are used in either cash or bank-related transactions.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @param voucherTypeIds The list of voucher type IDs to consider.
	 * @return An integer indicating the result of the check; typically 0 or 1, where 1 signifies usage in cash or bank.
	 */
	@Override
	public Integer isUseInCashOrBank(long id, List<Integer> voucherTypeIds) {
		return cashPaymentReceiptNativeQueryService.isUseInCashOrBank(id, voucherTypeIds);
	}

	/**
	 * Updates the list of CashPaymentReceiptResponseDto records by setting the supplier contact details
	 * and total amount for each record. The supplier contact details and total amount are fetched
	 * using the cashPaymentReceiptNativeQueryService based on the ID of each record.
	 *
	 * @param cashPaymentReceiptRecords The list of CashPaymentReceiptResponseDto records to be updated.
	 * 									Each record will have its contactSupplier and totalAmount attributes populated based on data
	 * 									retrieved from the cashPaymentReceiptNativeQueryService.
	 */
	private void setSupplierContactsAndAmountForRecords(List<CashPaymentReceiptResponseDto> cashPaymentReceiptRecords) {
		cashPaymentReceiptRecords.forEach(data -> {
			CashBankPaymentReceiptCommonResultListDto contactList = cashPaymentReceiptNativeQueryService.getSuppilerContact(data.getId());
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
	 * Filters a list of cash payment receipt response DTOs based on a query string.
	 * The filtering is performed on the supplier's name in default, preferred, or supportive language,
	 * or the formatted transaction date (in different date formats).
	 * Only receipts that match the query string in any of these fields are included in the result.
	 *
	 * @param receipts The list of CashPaymentReceiptResponseDto objects to be filtered.
	 * @param query The query string used for filtering receipts. It is case-insensitive and applied
	 *				to supplier names or transaction dates in various formats.
	 * @return A filtered list of CashPaymentReceiptResponseDto objects that match the query.
	 */
	private List<CashPaymentReceiptResponseDto> filterReceiptsBySupplierName(List<CashPaymentReceiptResponseDto> receipts, String query) {
		return receipts.stream().filter(data ->
				(data.getContactSupplier() != null && (
					(data.getContactSupplier().getNameDefaultLang() != null && data.getContactSupplier().getNameDefaultLang().toLowerCase().contains(query.toLowerCase())) ||
					(data.getContactSupplier().getNamePreferLang() != null && data.getContactSupplier().getNamePreferLang().toLowerCase().contains(query.toLowerCase())) ||
					(data.getContactSupplier().getNameSupportiveLang() != null && data.getContactSupplier().getNameSupportiveLang().toLowerCase().contains(query.toLowerCase()))
				)) ||
				data.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getTransactionDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString().toLowerCase().contains(query.toLowerCase()) ||
				data.getTotalAmount().toString().toLowerCase().contains(query.toLowerCase())
			)
			.collect(Collectors.toList());
	}

	/**
	 * Validates the fields of the provided CashPaymentReceiptRequestDto object.
	 * This method checks for missing or invalid values in the transaction date and
	 * cash payment receipt details list. If validation fails, it generates an error and throws an exception.
	 *
	 * @param cashPaymentReceiptRequestDto The data transfer object that contains the details of the
	 *									   cash payment receipt to be validated.
	 */
	private void validateFields(CashPaymentReceiptRequestDto cashPaymentReceiptRequestDto) {
		ErrorGenerator error = ErrorGenerator.builder();
		if (cashPaymentReceiptRequestDto.getTransactionDate() == null) {
			error.putError(FieldConstants.CASH_PAYMENT_RECEIPT_TRANSACTION_DATE, messageService.getMessage(MessagesConstant.VALIDATION_CASH_PAYMENT_RECEIPT_TRANSACTION_DATE));
		}
		if (cashPaymentReceiptRequestDto.getCashPaymentReceiptDetailsList().isEmpty()) {
			error.putError(FieldConstants.CASH_PAYMENT_RECEIPT_DETAILS_LIST, messageService.getMessage(MessagesConstant.VALIDATION_CASH_PAYMENT_RECEIPT_DETAILS_LIST));
		}
		if (error.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), error.getErrors());
		}
	}

	/**
	 * Sorts a list of CashPaymentReceiptResponseDto objects based on the specified field and direction.
	 *
	 * @param sortByField     the field by which to sort (supports nested contact fields and basic fields)
	 * @param filteredReceipts the list of receipts to sort
	 * @param sortDirection   the direction of sorting ("asc" or "desc")
	 */
	private void getSortedData(String sortByField, List<CashPaymentReceiptResponseDto> filteredReceipts, String sortDirection) {
		if (Objects.nonNull(sortByField) && StringUtils.isNotBlank(sortByField)) {
			Comparator<CashPaymentReceiptResponseDto> comparator = null;
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
				comparator = Comparator.comparing(CashPaymentReceiptResponseDto::getTotalAmount);
				break;
			case FieldConstants.TRANSACTION_DATE:
				comparator = Comparator.comparing(CashPaymentReceiptResponseDto::getTransactionDate);
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