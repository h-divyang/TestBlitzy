package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.PurchaseBillGetByIdDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRequestDto;
import com.catering.dto.tenant.request.PurchaseBillResponseDto;
import com.catering.model.tenant.PurchaseBillModel;
import com.catering.service.common.GenericService;

/**
 * The PurchaseBillService interface defines operations for managing purchase bills,
 * including creating, updating, retrieving, and deleting purchase bill entities.
 * It also provides methods for obtaining related information such as drop-down data
 * for purchase orders and raw materials.
 */
public interface PurchaseBillService extends GenericService<PurchaseBillResponseDto, PurchaseBillModel, Long> {

	/**
	 * Creates and updates a purchase bill based on the provided purchase bill request data.
	 *
	 * @param purchaseBillRequestDto The data transfer object containing details about the purchase bill to be created or updated.
	 * @return An Optional containing the created or updated PurchaseBillRequestDto if successful,
	 *         or an empty Optional otherwise.
	 */
	Optional<PurchaseBillRequestDto> createAndUpdate(PurchaseBillRequestDto purchaseBillRequestDto);

	/**
	 * Reads and retrieves a list of purchase bill responses based on the provided filter criteria.
	 * The response includes metadata and the filtered list of purchase bills wrapped in a response container.
	 *
	 * @param filterDto The filter criteria used to retrieve the purchase bills. It includes properties such as
	 *					pagination, sorting, and optional query parameters for filtering.
	 * @return A ResponseContainerDto containing a list of PurchaseBillResponseDto objects and additional metadata, such as status and messages.
	 */
	ResponseContainerDto<List<PurchaseBillResponseDto>> read(FilterDto filterDto);

	/**
	 * Retrieves a purchase bill by its unique identifier.
	 *
	 * @param id The unique identifier of the purchase bill to be retrieved.
	 * @return A PurchaseBillGetByIdDto containing the details of the purchase bill if found.
	 */
	PurchaseBillGetByIdDto getById(Long id);

	/**
	 * Deletes a purchase bill entity with the provided unique identifier.
	 *
	 * @param id The unique identifier of the purchase bill to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves data for a drop down representing purchase orders based on the provided identifier.
	 * The response contains a list of purchase order drop down data wrapped in a response container.
	 *
	 * @param id The unique identifier used to retrieve the purchase order drop down data.
	 * @return A ResponseContainerDto containing a list of PurchaseBillOrderDropDownDto objects along with meta-information such as status and messages.
	 */
	ResponseContainerDto<List<PurchaseBillOrderDropDownDto>> getPurchaseOrderDropDownData(Long id);

	/**
	 * Retrieves a list of raw materials associated with a purchase bill order based on the provided unique identifier.
	 *
	 * @param id The unique identifier of the purchase bill order for which raw material details are to be retrieved.
	 * @return A ResponseContainerDto containing a list of PurchaseBillOrderRawMaterialDto objects that include raw material details
	 *		   such as rawMaterialId, hsnCode, weight, measurementId, price, taxMasterId, and totalAmount, along with metadata like status and messages.
	 */
	ResponseContainerDto<List<PurchaseBillOrderRawMaterialDto>> getPurchaseBillOrderRawMaterial(Long id);

	/**
	 * Retrieves raw material data for populating a drop-down selection.
	 *
	 * @return A ResponseContainerDto containing a list of PurchaseBillRawMaterialDropDownDto objects,
	 *		   which represent raw material details for drop-downs, along with metadata such as status and messages.
	 */
	ResponseContainerDto<List<PurchaseBillRawMaterialDropDownDto>> getRawMaterialDropDownData();

}