package com.catering.dao.order;

import java.util.List;
import java.util.Map;

import com.catering.dto.tenant.request.DishCostingDto;
import com.catering.dto.tenant.request.TaxMasterDto;

/**
 * Service interface for handling business logic related to Book Orders and dish costing.
 * This interface defines methods to manage dish costing details, tax parameters, and common parameters for reports.
 */
public interface BookOrderNativeQueryService {

	/**
	 * Retrieves the dish costing details for a given order ID.
	 * 
	 * @param orderId The ID of the order for which dish costing details are to be fetched.
	 * @return A list of {@link DishCostingDto} containing the dish costing information for the order.
	 */
	List<DishCostingDto> dishCostingByOrderId(Long orderId);

	/**
	 * Sets the tax parameters for a report based on the provided tax master details and total amount.
	 * 
	 * @param taxMaster The {@link TaxMasterDto} containing tax details.
	 * @param isIgstRequired A flag indicating whether IGST is required.
	 * @param total The total amount to apply taxes on.
	 * @param parameters A map of parameters to set the tax values in.
	 */
	void setTaxParameters(TaxMasterDto taxMaster, boolean isIgstRequired, double total, Map<String, Object> parameters);

	/**
	 * Sets all parameters in the given map to zero.
	 * 
	 * @param parameters A map of parameters to be updated.
	 */
	void setParameterAsZero(Map<String, Object> parameters);

	/**
	 * Sets the common parameters for the report, including language type.
	 * 
	 * @param parameters A map of parameters to be updated.
	 * @param langType The language type (e.g., 1 for English, 2 for Hindi).
	 */
	void setCommonParameters(Map<String, Object> parameters, Integer langType, String reportName);

}