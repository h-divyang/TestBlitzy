package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.CustomRangeModel;

public interface CustomRangeRepository extends JpaRepository<CustomRangeModel, Long> {

	/**
	 * Retrieves a list of custom range records associated with a given measurement unit ID.
	 * 
	 * @param measurementId The ID of the measurement unit for which the custom range records need to be retrieved.
	 * @return A list of {@link CustomRangeModel} objects associated with the given measurement unit.
	 */
	List<CustomRangeModel> findByMeasurementId(Long measurementId);

}