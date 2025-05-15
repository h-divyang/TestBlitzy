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
import com.catering.dao.purchase_bill.PurchaseBillNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseBillGetByIdDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRequestDto;
import com.catering.dto.tenant.request.PurchaseBillResponseDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.enums.VoucherTypeEnum;
import com.catering.model.tenant.PurchaseBillModel;
import com.catering.model.tenant.PurchaseBillRawMaterialModel;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.PurchaseBillRepository;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CashPaymentReceiptService;
import com.catering.service.tenant.PurchaseBillService;
import com.catering.util.DataUtils;
import com.catering.util.NumberUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing Purchase Bills. This class handles
 * the business logic for operations related to Purchase Bills such as
 * creating, updating, reading, deleting, and handling dropdown data.
 *
 * Extends the GenericServiceImpl for common CRUD operations.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseBillServiceImpl extends GenericServiceImpl<PurchaseBillResponseDto, PurchaseBillModel, Long> implements PurchaseBillService {

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing purchase bills.
	 */
	PurchaseBillRepository purchaseBillRepository;

	/**
	 * Service for purchase bill native queries.
	 */
	PurchaseBillNativeQueryService purchaseBillNativeQueryService;

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for tax master data.
	 */
	TaxMasterRepository taxMasterRepository;

	/**
	 * Repository for managing contact information.
	 */
	ContactRepository contactRepository;

	/**
	 * Service for handling cash payment receipts.
	 */
	CashPaymentReceiptService cashPaymentReceiptService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<PurchaseBillRequestDto> createAndUpdate(PurchaseBillRequestDto purchaseBillRequestDto) {
		PurchaseBillModel purchaseBillModel = modelMapperService.convertEntityAndDto(purchaseBillRequestDto, PurchaseBillModel.class);
		validateCalculationFields(purchaseBillModel);
		if (Objects.nonNull(purchaseBillModel.getPurchaseBillRawMaterialList())) {
			purchaseBillModel.getPurchaseBillRawMaterialList().forEach(rawMaterial -> rawMaterial.setPurchaseBill(purchaseBillModel));
		}
		DataUtils.setAuditFields(purchaseBillRepository, purchaseBillRequestDto.getId(), purchaseBillModel);
		PurchaseBillRequestDto purchaseBillResponseDto = modelMapperService.convertEntityAndDto(purchaseBillRepository.save(purchaseBillModel), PurchaseBillRequestDto.class);
		if (Objects.nonNull(purchaseBillRequestDto.getId())) {
			purchaseBillRepository.calculateAccountBalance(1l, purchaseBillResponseDto.getId());
		} else {
			purchaseBillRepository.calculateAccountBalance(0l, purchaseBillResponseDto.getId());
		}
		return Optional.of(purchaseBillResponseDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseBillResponseDto>> read(FilterDto filterDto) {
		String query = filterDto.getQuery();
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if (StringUtils.isNotBlank(filterDto.getQuery()) || StringUtils.isNotBlank(filterDto.getSortBy())) {
			// Get all records of purchase bill
			ResponseContainerDto<List<PurchaseBillResponseDto>> allPurchaseBill = RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(purchaseBillRepository.findAll(), PurchaseBillResponseDto.class));
			getCalculation(allPurchaseBill.getBody());
			List<PurchaseBillResponseDto> purchaseBillFilteredRecord = filterDateContactAmount(allPurchaseBill.getBody(), query);
			getSortedData(filterDto.getSortBy(), purchaseBillFilteredRecord, filterDto.getSortDirection());
			// Convert to Page so we can reuse getPaging()
			int start = Math.min((Integer.parseInt(filterDto.getCurrentPage()) - 1) * (Integer.parseInt(filterDto.getSizePerPage())), purchaseBillFilteredRecord.size());
			int end = Math.min(start + (Integer.parseInt(filterDto.getSizePerPage())), purchaseBillFilteredRecord.size());

			Page<PurchaseBillResponseDto> pages = new PageImpl<>(purchaseBillFilteredRecord.subList(start, end), PageRequest.of((Integer.parseInt(filterDto.getCurrentPage()) - 1), (Integer.parseInt(filterDto.getSizePerPage()))), purchaseBillFilteredRecord.size());
			// Now use your existing paging method
			Optional<Paging> paging = PagingUtils.getPaging(pages);
			// Build the response
			ResponseContainerDto<List<PurchaseBillResponseDto>> result = RequestResponseUtils.generateResponseDto(pages.getContent());
			result.setPaging(paging.orElse(Paging.builder().build()));
			return result;
		}
		ResponseContainerDto<List<PurchaseBillResponseDto>> purchaseBillResponseDto = read(PurchaseBillResponseDto.class, PurchaseBillModel.class, filterDto, Optional.empty());
		getCalculation(purchaseBillResponseDto.getBody());
		return purchaseBillResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PurchaseBillGetByIdDto getById(Long id) {
		Optional<PurchaseBillModel> purchaseBillModel = purchaseBillRepository.findById(id);
		CommonCalculationFieldDto purchaseBillCalculationFieldDto = purchaseBillNativeQueryService.getPurchaseBillListCalculation(id);
		PurchaseBillGetByIdDto purchaseBillGetByIdDto = modelMapperService.convertEntityAndDto(purchaseBillModel.isPresent() ? purchaseBillModel.get() : new PurchaseBillModel(), PurchaseBillGetByIdDto.class);
		purchaseBillGetByIdDto.setTotalAmount(purchaseBillCalculationFieldDto.getTotalAmount());
		return purchaseBillGetByIdDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		Integer count = cashPaymentReceiptService.isUseInCashOrBank(id, VoucherTypeEnum.getIds(VoucherTypeEnum.PURCHASE_BILL));
		if (Objects.nonNull(count) && count > 0) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		if (!purchaseBillRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			purchaseBillRepository.deleteById(id);
			purchaseBillRepository.calculateAccountBalance(2L, id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseBillOrderDropDownDto>> getPurchaseOrderDropDownData(Long id) {
		return RequestResponseUtils.generateResponseDto(purchaseBillNativeQueryService.getPurchaseBillOrderDropDown(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseBillOrderRawMaterialDto>> getPurchaseBillOrderRawMaterial(Long id) {
		return RequestResponseUtils.generateResponseDto(purchaseBillNativeQueryService.getPurchaseBillOrderRawMaterial(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseBillRawMaterialDropDownDto>> getRawMaterialDropDownData() {
		List<PurchaseBillRawMaterialDropDownDto> purchaseBillRawMaterialDropDownDtos = purchaseBillNativeQueryService.getPurchaseBillRawMaterial();
		purchaseBillRawMaterialDropDownDtos.forEach(purchaseBillRawMaterial -> {
			purchaseBillRawMaterial.setContactSupplierList(purchaseBillNativeQueryService.getPurchaseBillRawMaterialSupplier(purchaseBillRawMaterial.getId()));
		});
		return RequestResponseUtils.generateResponseDto(purchaseBillRawMaterialDropDownDtos);
	}

	/**
	 * Validates and calculates the fields required for a purchase bill. This method processes
	 * the raw material list of the provided purchase bill, calculating taxes and setting the total
	 * and grand total values accordingly.
	 *
	 * @param purchaseBillModel The PurchaseBillModel object containing details of the purchase bill to be validated.
	 *							This includes raw materials, taxes, discount, extra expenses, and other fields
	 *							necessary for calculating totals.
	 */
	private void validateCalculationFields(PurchaseBillModel purchaseBillModel) {
		List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
		boolean gstSameOrNot = contactRepository.isGstNumberSame(purchaseBillModel.getContactSupplier().getId());
		double total = 0D;
		double grandTotal = 0D;
		if (Objects.nonNull(purchaseBillModel.getPurchaseBillRawMaterialList())) {
			for (PurchaseBillRawMaterialModel rawMaterial : purchaseBillModel.getPurchaseBillRawMaterialList()) {
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
				total += NumberUtils.extractValue(rawMaterial.getTotalAmount()).doubleValue();
			}
		}
		grandTotal = total;
		grandTotal -= NumberUtils.extractValue(purchaseBillModel.getDiscount()).doubleValue();
		grandTotal += NumberUtils.extractValue(purchaseBillModel.getExtraExpense()).doubleValue();
		grandTotal += NumberUtils.extractValue(purchaseBillModel.getRoundOff()).doubleValue();
		purchaseBillModel.setGrandTotal(grandTotal);
		purchaseBillModel.setTotal(total);
	}

	/**
	 * Calculates and updates specific financial fields (amount, taxable amount, total amount) for a list of purchase bill response DTOs.
	 *
	 * @param purchaseBillResponseDtoList A list of {@link PurchaseBillResponseDto} objects for which the calculations need to be performed.
	 * @return A list of updated {@link PurchaseBillResponseDto} objects with calculated fields set.
	 */
	private List<PurchaseBillResponseDto> getCalculation(List<PurchaseBillResponseDto> purchaseBillResponseDtoList) {
		purchaseBillResponseDtoList.forEach(responseDto -> {
			CommonCalculationFieldDto purchaseBillCalculationFieldDto = purchaseBillNativeQueryService.getPurchaseBillListCalculation(responseDto.getId());
			responseDto.setAmount(purchaseBillCalculationFieldDto.getAmount());
			responseDto.setTaxableAmount(purchaseBillCalculationFieldDto.getTaxableAmount());
			responseDto.setTotalAmount(purchaseBillCalculationFieldDto.getTotalAmount());
		});
		return purchaseBillResponseDtoList;
	}

	/**
	 * Sorts a list of {@link PurchaseBillResponseDto} based on a specified field and sort direction.
	 *
	 * @param sortByField the field name used for sorting (e.g., "contact.nameDefaultLang", "totalAmount")
	 * @param purchaseOrderFilterdata the list of purchase bill records to sort
	 * @param sortDirection the sort direction, either "asc" for ascending or "desc" for descending
	 */
	private void getSortedData(String sortByField, List<PurchaseBillResponseDto> purchaseOrderFilterdata, String sortDirection) {
		if (Objects.nonNull(sortByField) && StringUtils.isNotBlank(sortByField)) {
			Comparator<PurchaseBillResponseDto> comparator = null;
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
				comparator = Comparator.comparing(PurchaseBillResponseDto::getBillNumber, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(PurchaseBillResponseDto::getId, Comparator.reverseOrder());
				break;
			case FieldConstants.TOTAL_AMOUNT:
				comparator = Comparator.comparing(PurchaseBillResponseDto::getTotalAmount);
				break;
			case FieldConstants.TAXABLE_AMOUNT:
				comparator = Comparator.comparing(PurchaseBillResponseDto::getTaxableAmount);
				break;
			case FieldConstants.AMOUNT:
				comparator = Comparator.comparing(PurchaseBillResponseDto::getAmount);
				break;
			case FieldConstants.PURCHASE_BILL_DATE:
				comparator = Comparator.comparing(PurchaseBillResponseDto::getBillDate);
				break;
			}

			// Apply sort if comparator is set
			if (comparator != null) {
				// Reverse comparator if sort direction is descending
				if ("desc".equalsIgnoreCase(sortDirection)) {
					comparator = comparator.reversed();
				}
				// Sort the list using the comparator
				purchaseOrderFilterdata.sort(comparator);
			}
		}
	}

	/**
	 * Filters a list of {@link PurchaseBillResponseDto} based on the search query.
	 * The query is matched against contact supplier names (in all languages), bill date (in various formats),
	 * bill number, and amount fields.
	 *
	 * @param purchaseBillRecords the list of purchase bill records to filter
	 * @param query the search query string
	 * @return a list of {@link PurchaseBillResponseDto} that match the query criteria
	 */
	private List<PurchaseBillResponseDto> filterDateContactAmount(List<PurchaseBillResponseDto> purchaseBillRecords, String query) {
		return purchaseBillRecords.stream().filter(data ->
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