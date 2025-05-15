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
import com.catering.dao.purchase_order.PurchaseOrderNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseOrderContactDto;
import com.catering.dto.tenant.request.PurchaseOrderGetByIdDto;
import com.catering.dto.tenant.request.PurchaseOrderRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseOrderRequestDto;
import com.catering.dto.tenant.request.PurchaseOrderResponseDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.model.tenant.PurchaseOrderModel;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.PurchaseOrderRepository;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.PurchaseOrderService;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing Purchase Orders. This class provides functionality
 * to create, update, retrieve, delete, and manage various operations related to purchase orders.
 * It extends {@code GenericServiceImpl} to reuse generic CRUD functionality and implements
 * {@code PurchaseOrderService} to define purchase order-specific operations.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderServiceImpl extends GenericServiceImpl<PurchaseOrderResponseDto, PurchaseOrderModel, Long> implements PurchaseOrderService {

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing purchase orders.
	 */
	PurchaseOrderRepository purchaseOrderRepository;

	/**
	 * Service for purchase order native queries.
	 */
	PurchaseOrderNativeQueryService purchaseOrderNativeQueryService;

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
	 * {@inheritDoc}
	 */
	@Override
	public Optional<PurchaseOrderRequestDto> createAndUpdate(PurchaseOrderRequestDto purchaseOrderRequestDto) {
		PurchaseOrderModel purchaseOrderModel = modelMapperService.convertEntityAndDto(purchaseOrderRequestDto, PurchaseOrderModel.class);
		validateCalculationFields(purchaseOrderModel);
		purchaseOrderModel.getPurchaseOrderRawMaterialList().forEach(rawMaterial -> rawMaterial.setPurchaseOrder(purchaseOrderModel));
		DataUtils.setAuditFields(purchaseOrderRepository, purchaseOrderRequestDto.getId(), purchaseOrderModel);
		return Optional.of(modelMapperService.convertEntityAndDto(purchaseOrderRepository.save(purchaseOrderModel), PurchaseOrderRequestDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseOrderResponseDto>> read(FilterDto filterDto) {
		String query = filterDto.getQuery();
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if (StringUtils.isNotBlank(filterDto.getQuery()) || StringUtils.isNotBlank(filterDto.getSortBy())) {
			// Get all records of purchase order
			ResponseContainerDto<List<PurchaseOrderResponseDto>> allPurchaseOrder = RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(purchaseOrderRepository.findAll(), PurchaseOrderResponseDto.class));
			getCalculation(allPurchaseOrder.getBody());
			List<PurchaseOrderResponseDto> purchaseOrderFilteredRecord = filterDateContactAmount(allPurchaseOrder.getBody(), query);
			getSortedData(filterDto.getSortBy(), purchaseOrderFilteredRecord, filterDto.getSortDirection());
			// Convert to Page so we can reuse getPaging()
			int start = Math.min((Integer.parseInt(filterDto.getCurrentPage()) - 1) * (Integer.parseInt(filterDto.getSizePerPage())), purchaseOrderFilteredRecord.size());
			int end = Math.min(start + (Integer.parseInt(filterDto.getSizePerPage())), purchaseOrderFilteredRecord.size());

			Page<PurchaseOrderResponseDto> pages = new PageImpl<>(purchaseOrderFilteredRecord.subList(start, end), PageRequest.of((Integer.parseInt(filterDto.getCurrentPage()) - 1), (Integer.parseInt(filterDto.getSizePerPage()))), purchaseOrderFilteredRecord.size());
			// Now use your existing paging method
			Optional<Paging> paging = PagingUtils.getPaging(pages);
			// Build the response
			ResponseContainerDto<List<PurchaseOrderResponseDto>> result = RequestResponseUtils.generateResponseDto(pages.getContent());
			result.setPaging(paging.orElse(Paging.builder().build()));
			return result;
		}
		ResponseContainerDto<List<PurchaseOrderResponseDto>> purchaseOrderResponseDto = read(PurchaseOrderResponseDto.class, PurchaseOrderModel.class, filterDto, Optional.empty());
		getCalculation(purchaseOrderResponseDto.getBody());
		return purchaseOrderResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!purchaseOrderRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			purchaseOrderRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseOrderRawMaterialDropDownDto>> getRawMaterialDropDownData() {
		List<PurchaseOrderRawMaterialDropDownDto> purchaseOrderRawMaterialDropDownDtos = purchaseOrderNativeQueryService.getRawMaterialList();
		purchaseOrderRawMaterialDropDownDtos.forEach(purchaseOrderRawMaterial ->
			purchaseOrderRawMaterial.setContactSupplierList(purchaseOrderNativeQueryService.getContactSupplierForItem(purchaseOrderRawMaterial.getId())));
		return RequestResponseUtils.generateResponseDto(purchaseOrderRawMaterialDropDownDtos);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<PurchaseOrderContactDto>> getContactDropDownData() {
		return RequestResponseUtils.generateResponseDto(purchaseOrderNativeQueryService.getContactList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PurchaseOrderGetByIdDto getById(Long id) {
		Optional<PurchaseOrderModel> purchaseOrderModel = purchaseOrderRepository.findById(id);
		CommonCalculationFieldDto purchaseOrderCalculationFieldDto = purchaseOrderNativeQueryService.getPurchaseListCalculation(id);
		PurchaseOrderGetByIdDto purchaseOrderGetByIdDto = modelMapperService.convertEntityAndDto(purchaseOrderModel.get(), PurchaseOrderGetByIdDto.class);
		purchaseOrderGetByIdDto.setTotalAmount(purchaseOrderCalculationFieldDto.getTotalAmount());
		return purchaseOrderGetByIdDto;
	}

	/**
	 * Validates and calculates the fields related to a purchase order's raw materials.
	 * This method ensures that the necessary calculations for total amounts are performed
	 * based on the provided purchase order model, its associated tax information, and GST rules.
	 *
	 * @param purchaseModel The purchase order model containing the list of raw materials and supplier information.
	 *						It must include a valid raw material list and supplier contact details.
	 */
	private void validateCalculationFields(PurchaseOrderModel purchaseModel) {
		List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
		boolean gstSameOrNot = contactRepository.isGstNumberSame(purchaseModel.getContactSupplier().getId());
		purchaseModel.getPurchaseOrderRawMaterialList().forEach(rawMaterial -> {
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

	/**
	 * Updates the calculation fields of each PurchaseOrderResponseDto in the given list.
	 * For each item in the list, it retrieves the associated calculation fields using the
	 * purchaseOrderNativeQueryService and updates the amount, taxableAmount, and totalAmount values.
	 *
	 * @param purchaseOrderResponseDtolist The list of PurchaseOrderResponseDto objects to be updated with calculated fields.
	 * @return A list of PurchaseOrderResponseDto objects with updated calculation fields.
	 */
	private List<PurchaseOrderResponseDto> getCalculation(List<PurchaseOrderResponseDto> purchaseOrderResponseDtolist) {
		purchaseOrderResponseDtolist.forEach(responseDto -> {
			CommonCalculationFieldDto purchaseOrderCalculationFieldDto = purchaseOrderNativeQueryService.getPurchaseListCalculation(responseDto.getId());
			responseDto.setAmount(purchaseOrderCalculationFieldDto.getAmount());
			responseDto.setTaxableAmount(purchaseOrderCalculationFieldDto.getTaxableAmount());
			responseDto.setTotalAmount(purchaseOrderCalculationFieldDto.getTotalAmount());
		});
		return purchaseOrderResponseDtolist;
	}

	/**
	 * Sorts the given list of {@link PurchaseOrderResponseDto} based on the specified field and direction.
	 *
	 * @param sortByField the field name to sort by (e.g., "contact.nameDefaultLang", "totalAmount")
	 * @param purchaseOrderFilterdata the list of purchase order data to be sorted
	 * @param sortDirection the direction of sorting: "asc" for ascending or "desc" for descending
	 */
	private void getSortedData(String sortByField, List<PurchaseOrderResponseDto> purchaseOrderFilterdata, String sortDirection) {
		if (Objects.nonNull(sortByField) && StringUtils.isNotBlank(sortByField)) {
			Comparator<PurchaseOrderResponseDto> comparator = null;
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
				comparator = Comparator.comparing(PurchaseOrderResponseDto::getTotalAmount);
				break;
			case FieldConstants.TAXABLE_AMOUNT:
				comparator = Comparator.comparing(PurchaseOrderResponseDto::getTaxableAmount);
				break;
			case FieldConstants.AMOUNT:
				comparator = Comparator.comparing(PurchaseOrderResponseDto::getAmount);
				break;
			case FieldConstants.PURCHASE_ORDER_DATE:
				comparator = Comparator.comparing(PurchaseOrderResponseDto::getPurchaseDate);
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
	 * Filters the list of {@link PurchaseOrderResponseDto} records by checking if the given query
	 * matches the contact supplier's name (in any language), purchase date (in multiple formats),
	 * or amount fields.
	 *
	 * @param purchaseOrderRecords the list of purchase order records to be filtered
	 * @param query the search query string used to filter the records
	 * @return a list of filtered {@link PurchaseOrderResponseDto} that match the query
	 */
	private List<PurchaseOrderResponseDto> filterDateContactAmount(List<PurchaseOrderResponseDto> purchaseOrderRecords, String query) {
		return purchaseOrderRecords.stream().filter(data ->
		(data.getContactSupplier() != null && (
				(data.getContactSupplier().getNameDefaultLang() != null && data.getContactSupplier().getNameDefaultLang().toLowerCase().contains(query.toLowerCase())) ||
				(data.getContactSupplier().getNamePreferLang() != null && data.getContactSupplier().getNamePreferLang().toLowerCase().contains(query.toLowerCase())) ||
				(data.getContactSupplier().getNameSupportiveLang() != null && data.getContactSupplier().getNameSupportiveLang().toLowerCase().contains(query.toLowerCase()))
			)) ||
			data.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getPurchaseDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getPurchaseDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getAmount().toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getTaxableAmount().toString().toLowerCase().contains(query.toLowerCase()) ||
			data.getTotalAmount().toString().toLowerCase().contains(query.toLowerCase())
		)
		.collect(Collectors.toList());
	}

}