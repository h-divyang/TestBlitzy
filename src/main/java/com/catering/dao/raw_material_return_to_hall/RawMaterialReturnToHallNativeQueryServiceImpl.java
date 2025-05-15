package com.catering.dao.raw_material_return_to_hall;

import java.util.List;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallCalculationDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link RawMaterialReturnToHallNativeQueryService} interface.
 * This service provides methods to retrieve and calculate raw material data related to input transfers
 * to halls, using the {@link RawMaterialReturnToHallNativeQueryDao} for data access.
 *
 * <p>Methods include fetching dropdown data for raw material return to hall input transfer, calculating raw material return data, 
 * and finding raw materials based on the input transfer to hall ID.</p>
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RawMaterialReturnToHallNativeQueryServiceImpl implements RawMaterialReturnToHallNativeQueryService {

	/**
	 * The DAO used to interact with the underlying database for raw material return to hall queries.
	 */
	RawMaterialReturnToHallNativeQueryDao rawMaterialReturnToHallNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialReturnToHallInputTransferToHallDropDownDto> getRawMaterialReturnToHallInputTransferToHallDropDownData(Long inputTransferToHallId) {
		return rawMaterialReturnToHallNativeQueryDao.getRawMaterialReturnToHallInputTransferToHallDropDownData(inputTransferToHallId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RawMaterialReturnToHallCalculationDto getRawMaterialReturnToHallCalculation(Long id) {
		return rawMaterialReturnToHallNativeQueryDao.getRawMaterialReturnToHallCalculation(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallUpcomingOrderRawMaterial> findRawMaterialByInputTransferToHallId(Long inputTransferToHallId) {
		return rawMaterialReturnToHallNativeQueryDao.findRawMaterialByInputTransferToHallId(inputTransferToHallId);
	}

}