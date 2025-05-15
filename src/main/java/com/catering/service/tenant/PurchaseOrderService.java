package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.PurchaseOrderContactDto;
import com.catering.dto.tenant.request.PurchaseOrderGetByIdDto;
import com.catering.dto.tenant.request.PurchaseOrderRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseOrderRequestDto;
import com.catering.dto.tenant.request.PurchaseOrderResponseDto;
import com.catering.model.tenant.PurchaseOrderModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing purchase orders. This interface extends
 * the GenericService for CRUD operations and includes additional domain-specific
 * methods for handling various purchase order-related operations.
 *
 * @param <Dto> The DTO type.
 * @param <Model> The model type.
 * @param <Id> The ID type.
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
public interface PurchaseOrderService extends GenericService<PurchaseOrderResponseDto, PurchaseOrderModel, Long> {

	/**
	 * Creates or updates a purchase order request DTO.
	 * 
	 * @param purchaseOrderRequestDto The purchase order request DTO.
	 * @return An optional containing the created or updated purchase order request DTO.
	 */
	Optional<PurchaseOrderRequestDto> createAndUpdate(PurchaseOrderRequestDto purchaseOrderRequestDto);

	/**
	 * Reads purchase order response DTOs based on the provided filter DTO.
	 * 
	 * @param filterDto The filter DTO.
	 * @return A response container DTO containing a list of purchase order response DTOs.
	 */
	ResponseContainerDto<List<PurchaseOrderResponseDto>> read(FilterDto filterDto);

	/**
	 * Get a purchase order by ID.
	 * 
	 * @param id The ID of the purchase order to get.
	 */
	PurchaseOrderGetByIdDto getById(Long id);

	/**
	 * Deletes a purchase order by ID.
	 * 
	 * @param id The ID of the purchase order to delete.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves the drop-down data for purchase order raw materials.
	 * 
	 * @return A response container DTO containing a list of purchase order raw material drop-down DTOs.
	 */
	ResponseContainerDto<List<PurchaseOrderRawMaterialDropDownDto>> getRawMaterialDropDownData();

	/**
	 * Retrieves the drop-down data for purchase order contacts.
	 * 
	 * @return A response container DTO containing a list of purchase order contact DTOs.
	 */
	ResponseContainerDto<List<PurchaseOrderContactDto>> getContactDropDownData();

}