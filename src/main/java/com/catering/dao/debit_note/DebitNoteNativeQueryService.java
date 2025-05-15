package com.catering.dao.debit_note;

import java.util.List;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;

/**
 * Service interface for managing and retrieving Debit Note-related data using native queries.
 * This service provides methods to fetch dropdown options, raw material details,
 * and perform calculations specific to Debit Notes.
 */
public interface DebitNoteNativeQueryService {

	/**
	 * Retrieves a list of dropdown data for Debit Note purchase bills associated with a given ID.
	 *
	 * @param id the ID of the Debit Note for which the purchase bill dropdown data is retrieved
	 * @return a list of {@link DebitNotePurchaseBillDropDownDto} containing the dropdown data
	 */
	List<DebitNotePurchaseBillDropDownDto> getDebitNotePurchaseBillDropDown(Long id);

	/**
	 * Retrieves raw material details associated with a specific purchase bill ID for a Debit Note.
	 *
	 * @param purchaseBillId the ID of the purchase bill for which raw material details are retrieved
	 * @return a list of {@link PurchaseBillOrderRawMaterialDto} containing the raw material details
	 */
	List<PurchaseBillOrderRawMaterialDto> getDebitNotePurchaseBillRawMaterial(Long purchaseBillId);

	/**
	 * Retrieves common calculation fields for a Debit Note associated with the given ID.
	 *
	 * @param id the ID of the Debit Note for which calculation fields are retrieved
	 * @return a {@link CommonCalculationFieldDto} object containing the calculation details
	 */
	CommonCalculationFieldDto getDebitNoteListCalculation(Long id);

	/**
	 * Retrieves a list of dropdown data for raw materials used in Debit Notes.
	 *
	 * @return a list of {@link DebitNoteRawMaterialDropDownDto} containing the raw material dropdown data
	 */
	List<DebitNoteRawMaterialDropDownDto> getDebitNoteRawMaterial();

}