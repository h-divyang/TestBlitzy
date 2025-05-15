package com.catering.dao.order_reports.combine_report;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.CombineReportRequestParmDto;

public interface CombineReportQueryService {

	/**
	 * Generates a combined report for an order based on the provided parameters.
	 * 
	 * @param orderId the ID of the order for which to generate the combined report
	 * @param dto the parameters specifying which reports to include and their configurations
	 * @param request the HTTP request containing tenant-specific information
	 * @return a {@link FileBean} containing the generated combined report as a byte array
	 */
	FileBean generateCombineReport(Long orderId, CombineReportRequestParmDto dto, HttpServletRequest request);

}