package com.catering.dao.order;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.catering.constant.Constants;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_bank.CompanyBankNativeQueryService;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dao.company_user.CompanyUserNativeQueryService;
import com.catering.dto.tenant.request.CompanyBankForReportDto;
import com.catering.dto.tenant.request.DishCostingDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.service.common.FileService;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.NumberUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for handling business logic related to Book Orders and dish costing.
 * This class implements the {@link BookOrderNativeQueryService} interface and provides methods
 * to manage dish costing details, tax parameters, and common parameters for reports.
 * It interacts with various DAO and service layers to fetch and process the required data.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookOrderNativeQueryServiceImpl implements BookOrderNativeQueryService {

	/**
	 * DAO interface for executing native queries related to Book Orders.
	 */
	BookOrderNativeQueryDao orderNativeQueryDao;

	/**
	 * Service for managing company user-related queries and operations.
	 */
	CompanyUserNativeQueryService companyUserNativeQueryService;

	/**
	 * Service for managing company preferences and settings.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Service for managing company bank-related queries and operations.
	 */
	CompanyBankNativeQueryService companyBankNativeQueryService;

	/**
	 * Service for handling file-related operations, such as file generation or storage.
	 */
	FileService fileService;

	/**
	 * Service for generating and handling JasperReports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DishCostingDto> dishCostingByOrderId(Long orderId) {
		return orderNativeQueryDao.dishCostingByOrderId(orderId, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaxParameters(TaxMasterDto taxMaster, boolean isIgstRequired, double total, Map<String, Object> parameters) {
		double cgst = isIgstRequired ? 0.0 : taxMaster.getCgst();
		double sgst = isIgstRequired ? 0.0 : taxMaster.getSgst();
		double igst = isIgstRequired ? taxMaster.getIgst() : 0.0;

		parameters.put(ReportParameterConstants.SGST, sgst);
		parameters.put(ReportParameterConstants.SGST_TOTAL, Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(total * sgst / 100.0)));
		parameters.put(ReportParameterConstants.CGST, cgst);
		parameters.put(ReportParameterConstants.CGST_TOTAL, Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(total * cgst / 100.0)));
		parameters.put(ReportParameterConstants.IGST, igst);
		parameters.put(ReportParameterConstants.IGST_TOTAL, Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(total * igst / 100.0)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterAsZero(Map<String, Object> parameters) {
		parameters.put(ReportParameterConstants.SGST_TOTAL, 0.0);
		parameters.put(ReportParameterConstants.SGST, 0.0);
		parameters.put(ReportParameterConstants.CGST_TOTAL, 0.0);
		parameters.put(ReportParameterConstants.CGST, 0.0);
		parameters.put(ReportParameterConstants.IGST_TOTAL, 0.0);
		parameters.put(ReportParameterConstants.IGST, 0.0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCommonParameters(Map<String, Object> parameters, Integer langType, String reportName) {
		jasperReportService.setBackgroundImageInReport(parameters);
		jasperReportService.setDecimalPatterns(parameters);
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		parameters.put(Constants.QRCODE.toUpperCase(), fileService.getUrl(Constants.QRCODE));
		CompanyBankForReportDto companyBankForReportDto = companyBankNativeQueryService.find(DataUtils.getLangType(langType));
		if (Objects.nonNull(companyBankForReportDto)) {
			parameters.put(ReportParameterConstants.ACCOUNT_NAME, Objects.nonNull(companyBankForReportDto.getAccountName()) ? companyBankForReportDto.getAccountName() : "");
			parameters.put(ReportParameterConstants.ACCOUNT_NUMBER, Objects.nonNull(companyBankForReportDto.getAccountNumber()) ? companyBankForReportDto.getAccountNumber() : "");
			parameters.put(ReportParameterConstants.BRANCH_NAME, Objects.nonNull(companyBankForReportDto.getBranchName()) ? companyBankForReportDto.getBranchName() : "");
			parameters.put(ReportParameterConstants.IFSC_CODE, Objects.nonNull(companyBankForReportDto.getIfscCode()) ? companyBankForReportDto.getIfscCode() : "");
		}
	}

}