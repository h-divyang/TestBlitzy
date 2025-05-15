package com.catering.dao.debit_note;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;

/**
 * DAO interface for executing native queries defined in {@code DebitNoteNativeQuery}.
 * 
 * <p>This interface provides methods to fetch data related to debit notes, purchase bills, raw materials,
 * and calculations by leveraging predefined named native queries.
 * 
 * <h2>Methods:</h2>
 * <ul>
 * <li>{@code getDebitNotePurchaseBillDropDown(Long id)}: Fetches dropdown data for purchase bills not linked to debit notes or specific IDs.</li>
 * <li>{@code getDebitNotePurchaseBillRawMaterial(Long purchaseBillId)}: Retrieves raw material data for a specified purchase bill ID.</li>
 * <li>{@code getDebitNoteListCalculation(Long id)}: Fetches calculation details for a specific debit note ID.</li>
 * <li>{@code getDebitNoteRawMaterial()}: Retrieves a list of active raw materials for a dropdown.</li>
 * </ul>
 * 
 * <h2>Query Details:</h2>
 * <ul>
 * <li><b>debitNotePurchaseBillDropDown</b>: Native query fetching purchase bill details.</li>
 * <li><b>debitNotePurchaseBillRawMaterial</b>: Native query fetching raw material details for purchase bills.</li>
 * <li><b>debitNoteListCalculation</b>: Native query performing calculations for debit notes.</li>
 * <li><b>debitNodeRawMaterialDropDown</b>: Native query fetching raw material dropdown details.</li>
 * </ul>
 * 
 * @see DebitNoteNativeQuery
 */
public interface DebitNoteNativeQueryDao extends JpaRepository<DebitNoteNativeQuery, Long> {

	/**
	 * Fetches dropdown data for purchase bills not linked to debit notes or specific IDs.
	 * 
	 * @param id the ID to include in the query results.
	 * @return a list of {@code DebitNotePurchaseBillDropDownDto} objects.
	 */
	@Query(name = "debitNotePurchaseBillDropDown", nativeQuery = true)
	List<DebitNotePurchaseBillDropDownDto> getDebitNotePurchaseBillDropDown(Long id);

	/**
	 * Retrieves raw material data for a specified purchase bill ID.
	 * 
	 * @param purchaseBillId the ID of the purchase bill.
	 * @return a list of {@code PurchaseBillOrderRawMaterialDto} objects.
	 */
	@Query(name = "debitNotePurchaseBillRawMaterial", nativeQuery = true)
	List<PurchaseBillOrderRawMaterialDto> getDebitNotePurchaseBillRawMaterial(Long purchaseBillId);

	/**
	 * Fetches calculation details for a specific debit note ID.
	 * 
	 * @param id the ID of the debit note.
	 * @return a {@code CommonCalculationFieldDto} object containing calculation details.
	 */
	@Query(name = "debitNoteListCalculation", nativeQuery = true)
	CommonCalculationFieldDto getDebitNoteListCalculation(Long id);

	/**
	 * Retrieves a list of active raw materials for a dropdown.
	 * 
	 * @return a list of {@code DebitNoteRawMaterialDropDownDto} objects.
	 */
	@Query(name = "debitNodeRawMaterialDropDown", nativeQuery = true)
	List<DebitNoteRawMaterialDropDownDto> getDebitNoteRawMaterial();

}