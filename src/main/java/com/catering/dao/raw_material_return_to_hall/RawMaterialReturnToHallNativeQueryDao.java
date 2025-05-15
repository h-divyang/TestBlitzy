package com.catering.dao.raw_material_return_to_hall;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallCalculationDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;

/**
 * Entity class representing raw material return to hall operations.
 * This class utilizes named native queries to retrieve and calculate data 
 * related to raw material returns, input transfers to halls, and associated 
 * raw material details from the database.
 *
 * <p>This class is annotated with native SQL queries that interact with multiple 
 * database tables such as <code>input_transfer_to_hall</code>, <code>hall_master</code>, 
 * <code>raw_material_return_to_hall</code>, <code>measurement</code>, etc. The queries 
 * handle various operations like fetching data for dropdowns, calculating material weights, 
 * and returning material details with unit conversions.</p>
 */
public interface RawMaterialReturnToHallNativeQueryDao extends JpaRepository<RawMaterialReturnToHallNativeQuery, Long> {

	/**
	 * Fetches input transfer to hall data that has not been returned or is included
	 * in the specified input transfer to hall ID.
	 * 
	 * @param inputTransferToHallId the ID of the input transfer to hall
	 * @return a list of {@link RawMaterialReturnToHallInputTransferToHallDropDownDto} objects
	 */
	@Query(name = "getRawMaterialReturnToHallInputTransferToHallDropDownData", nativeQuery = true)
	List<RawMaterialReturnToHallInputTransferToHallDropDownDto> getRawMaterialReturnToHallInputTransferToHallDropDownData(Long inputTransferToHallId);

	/**
	 * Retrieves raw material details linked to a specific input transfer to hall ID.
	 * 
	 * @param inputTransferToHallId the ID of the input transfer to hall
	 * @return a list of {@link InputTransferToHallUpcomingOrderRawMaterial} objects
	 */
	@Query(name = "findRawMaterialByInputTransferToHallId", nativeQuery = true)
	List<InputTransferToHallUpcomingOrderRawMaterial> findRawMaterialByInputTransferToHallId(Long inputTransferToHallId);

	/**
	 * Retrieves the calculated return data for raw materials, including weight, unit 
	 * conversions, and formatted for different languages.
	 * 
	 * @param id the ID of the raw material return to hall entry
	 * @return a {@link RawMaterialReturnToHallCalculationDto} object containing the calculation result
	 */
	@Query(name = "getRawMaterialReturnToHallCalculation", nativeQuery = true)
	RawMaterialReturnToHallCalculationDto getRawMaterialReturnToHallCalculation(Long id);

}