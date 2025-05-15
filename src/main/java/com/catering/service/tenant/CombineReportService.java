package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.request.CombineReportDto;

public interface CombineReportService {

	/**
	 * Creates and updates combined reports based on the provided list of report configurations.
	 * 
	 * @param combineReportRequest list of {@link CombineReportDto} containing the report configurations to save
	 */
	void createAndUpdate(List<CombineReportDto> combineReportRequest);

	/**
	 * Reads all combined reports.
	 * 
	 * @return list of {@link CombineReportDto} containing the report configurations
	 */
	List<CombineReportDto> read();

}
