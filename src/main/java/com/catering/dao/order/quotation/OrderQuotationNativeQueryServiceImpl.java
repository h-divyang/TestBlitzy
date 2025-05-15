package com.catering.dao.order.quotation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.constant.Constants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dao.order.BookOrderNativeQueryDao;
import com.catering.dao.order.BookOrderNativeQueryService;
import com.catering.dto.tenant.request.CompanyPreferencesForReportDto;
import com.catering.dto.tenant.request.OrderDtoForReport;
import com.catering.dto.tenant.request.OrderQuotationFunctionRequestDto;
import com.catering.dto.tenant.request.OrderQuotationReportDto;
import com.catering.dto.tenant.request.OrderQuotationRequestDto;
import com.catering.dto.tenant.request.OrderQuotationResponseDto;
import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.model.tenant.OrderQuotationModel;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.OrderQuotationRepository;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.JasperReportService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.ReportCompanyDetailRightsService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.NumberToStringUtils;
import com.catering.util.NumberUtils;
import com.catering.util.StringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link OrderQuotationNativeQueryService} interface for managing order quotations.
 * 
 * <p>This service provides business logic for creating, updating, retrieving, and generating reports 
 * related to order quotations. It interacts with various data access objects and repositories to 
 * facilitate the required operations.</p>
 * 
 * <p>Key responsibilities of this class include:</p>
 * <ul>
 * <li>Managing order quotation data in the database using {@link OrderQuotationNativeQueryDao} and {@link OrderQuotationRepository}.</li>
 * <li>Transforming data between DTOs and entities using {@link ModelMapperService}.</li>
 * <li>Retrieving tax details via {@link TaxMasterRepository} and customer contact information via {@link ContactRepository}.</li>
 * <li>Generating formatted reports with {@link JasperReportService}.</li>
 * <li>Incorporating company preferences into report generation through {@link CompanyPreferencesNativeQueryService} and related implementations.</li>
 * </ul>
 * 
 * <p>The class is annotated with:</p>
 * <ul>
 * <li>{@link Service} to mark it as a Spring-managed service component.</li>
 * <li>{@link RequiredArgsConstructor} to automatically inject dependencies defined as final fields.</li>
 * <li>{@link FieldDefaults} to set the visibility and finality of fields at the class level.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderQuotationNativeQueryServiceImpl implements OrderQuotationNativeQueryService {

	/**
	 * DAO interface for handling database operations related to the {@link OrderQuotationNativeQuery} entity.
	 * It provides methods to interact with the database and fetch order quotations based on the order ID.
	 */
	OrderQuotationNativeQueryDao orderQuotationNativeQueryDao;

	/**
	 * Service for mapping data between entities and Data Transfer Objects (DTOs).
	 * It simplifies the transformation of data between different layers of the application.
	 */
	ModelMapperService modelMapperService;

	/**
	 * DAO interface for handling database operations related to book orders.
	 * It provides methods to fetch order details and generate related reports.
	 */
	BookOrderNativeQueryDao bookOrderNativeQueryDao;

	/**
	 * Repository interface for performing CRUD operations on the {@link OrderQuotation} entity.
	 * It interacts with the database to manage order quotations and their associated data.
	 */
	OrderQuotationRepository orderQuotationRepository;

	/**
	 * Repository interface for accessing tax master data.
	 * It provides methods to fetch tax rates (SGST, CGST, IGST) based on business logic.
	 */
	TaxMasterRepository taxMasterRepository;

	/**
	 * Repository interface for managing contacts within the application.
	 * It includes methods for validating GST numbers and retrieving contact information.
	 */
	ContactRepository contactRepository;

	/**
	 * Service for generating reports using the JasperReports framework.
	 * It provides methods to configure and generate reports, including setting company logos and applying formats.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service interface for fetching company preferences related to report generation.
	 * It retrieves company information such as name, address, and contact details for use in reports.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * Implementation of the {@link CompanyPreferencesNativeQueryService} interface.
	 * It provides the logic for fetching and applying company preferences for report generation.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Repository interface for accessing company preferences stored in the database.
	 * It handles operations for retrieving and modifying company-related configuration.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/**
	 * Service interface for handling operations related to book orders.
	 * It provides methods to fetch order data, calculate totals, and generate reports for book orders.
	 */
	BookOrderNativeQueryService bookOrderNativeQueryService;

	ReportCompanyDetailRightsService reportCompanyDetailRightsService;

	String COMPANY_NAME = "COMPANY_NAME";
	String COMPANY_ADDRESS = "COMPANY_ADDRESS";
	String COMPANY_EMAIL = "COMPANY_EMAIL";
	String COMPANY_MOBILE_NUMBER = "COMPANY_MOBILE_NUMBER";
	String EVENT_NAME = "EVENT_NAME";
	String VENUE = "VENUE";
	String CUSTOMER_MOBILE_NUMBER = "CUSTOMER_MOBILE_NUMBER";
	String COMPANY_OFFICE_NUMBER = "COMPANY_OFFICE_NUMBER";
	String EVENT_TIME = "EVENT_TIME";
	String CUSTOMER_GST_NO = "CUSTOMER_GST_NO";
	String CUSTOMER_EMAIL = "CUSTOMER_EMAIL";
	String TOTAL = "TOTAL";
	String COMPANY_GST_NO = "COMPANY_GST_NO";
	String REMARK = "REMARK";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderQuotationRequestDto createAndUpdate(OrderQuotationRequestDto orderQuotationRequestDto, Long customerContactId) {
		doCalculation(orderQuotationRequestDto, customerContactId);
		orderQuotationRequestDto.setIsActive(true);
		OrderQuotationModel orderQuotationModel = modelMapperService.convertEntityAndDto(orderQuotationRequestDto, OrderQuotationModel.class);
		orderQuotationModel.getFunctions().forEach(function -> function.setOrderQuotation(orderQuotationModel));
		DataUtils.setAuditFields(orderQuotationRepository, orderQuotationRequestDto.getId(), orderQuotationModel);
		if(Boolean.FALSE.equals(orderQuotationRequestDto.getIsRoughEstimation())) {
			orderQuotationModel.getFunctions().forEach((function) -> {
				function.setAmount("0");
			});
		}
		OrderQuotationModel orderQuotationModelResponse = orderQuotationRepository.save(orderQuotationModel);
		return modelMapperService.convertEntityAndDto(orderQuotationModelResponse, OrderQuotationRequestDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderQuotationResponseDto findOrderQuotationByOrderId(Long orderId) {
		OrderQuotationResponseDto orderQuotation = orderQuotationNativeQueryDao.findOrderQuotationByOrderId(orderId);
		if (companyPreferencesRepository.findSubscriptionId() == 2) {
			orderQuotation.setAdvancePayment(orderQuotation.getAdvancePayment() + orderQuotationRepository.getAdvancePayment(orderId));
		}
		orderQuotation.setFunctions(orderQuotationNativeQueryDao.findOrderQuotationFunctionByOrderId(orderId));
		if (Boolean.FALSE.equals(orderQuotation.getIsRoughEstimation())) {
			double totalAmount = orderQuotation.getFunctions() != null ? orderQuotation.getFunctions().stream().map(orderQuotationFunction -> orderQuotationFunction.getAmount()).filter(Objects::nonNull).mapToDouble(Double::parseDouble).sum(): 0.0;
			orderQuotation.setFunctionTotal(Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(totalAmount)));
		} else {
			orderQuotation.setFunctionTotal(0.0);
		}
		return orderQuotation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderQuotationReport(Long orderId, Long customerContactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Object tenant = request.getAttribute(Constants.TENANT);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.OrderReport.QuotationReport.class, StringUtils.dotSeparated(JasperParameterConstants.ORDER_REPORT, JasperParameterConstants.OrderReport.QUOTATION_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		setCompanyData(parameters, langType, reportName);
		NumberToStringUtils numberToStringUtils = new NumberToStringUtils();
		bookOrderNativeQueryService.setCommonParameters(parameters, langType, reportName);
		List<OrderQuotationReportDto> orderQuotationReport = orderQuotationNativeQueryDao.generateOrderQuotationReport(orderId, DataUtils.getLangType(langType));
		
		OrderDtoForReport order = bookOrderNativeQueryDao.findOrderForReport(orderId, DataUtils.getLangType(langType));
		if (Objects.nonNull(order)) {
			parameters.put(EVENT_NAME, order.getEventName());
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, order.getCustomerName());
			parameters.put(VENUE, order.getVenue());
			parameters.put(CUSTOMER_MOBILE_NUMBER, order.getCustomerMobileNumber());
			parameters.put(EVENT_TIME, order.getEventTime());
			parameters.put(CUSTOMER_GST_NO, order.getCustomerGstNo());
			parameters.put(CUSTOMER_EMAIL, order.getEmail());
			parameters.put(ReportParameterConstants.VENUE_VALUE, order.getVenue());
		}
		OrderQuotationResponseDto orderQuotation = orderQuotationNativeQueryDao.findOrderQuotationByOrderId(orderId);
		orderQuotation.setFunctions(orderQuotationNativeQueryDao.findOrderQuotationFunctionByOrderId(orderId));
		if (Boolean.FALSE.equals(orderQuotation.getIsRoughEstimation())) {
			double totalAmount = orderQuotation.getFunctions() != null ? orderQuotation.getFunctions().stream().map(orderQuotationFunction -> orderQuotationFunction.getAmount()).filter(Objects::nonNull).mapToDouble(Double::parseDouble).sum(): 0.0;
			orderQuotation.setFunctionTotal(Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(totalAmount)));
		} else {
			orderQuotation.setFunctionTotal(0.0);
		}
		// If tax is present then add required tax calculation for report 
		if (orderQuotation.getTaxMasterId() > 0) {
			List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
			boolean isIgstRequired = contactRepository.isGstNumberSame(customerContactId);
			Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), orderQuotation.getTaxMasterId())).findAny();
			if (taxMaster.isPresent()) {
				bookOrderNativeQueryService.setTaxParameters(taxMaster.get(), isIgstRequired, orderQuotation.getFunctionTotal(), parameters);
			}
		} else {
			bookOrderNativeQueryService.setParameterAsZero(parameters);
		}
		parameters.put(ReportParameterConstants.SUBTOTAL, orderQuotation.getFunctionTotal());
		parameters.put(ReportParameterConstants.DISCOUNT, orderQuotation.getDiscount());
		parameters.put(ReportParameterConstants.ROUND_OFF, orderQuotation.getRoundOff());
		parameters.put(ReportParameterConstants.GRAND_TOTAL, orderQuotationRepository.existsByOrderId(orderId) ? orderQuotation.getGrandTotal() : orderQuotation.getFunctionTotal());
		parameters.put(ReportParameterConstants.ADVANCE_PAYMENT, orderQuotation.getAdvancePayment());
		parameters.put(TOTAL, orderQuotation.getTotal());
		parameters.put(ReportParameterConstants.ROUGH_ESTIMATION, orderQuotation.getIsRoughEstimation());
		if (Objects.equals(tenant, Constants.SHREE_RAJ_CATERING)) {
			parameters.put(ReportParameterConstants.RUPEES_IN_WORDS_VALUE, numberToStringUtils.convertToWords(orderQuotation.getTotal().longValue()));	
		}
		parameters.put(REMARK, orderQuotation.getRemark());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ORDER_QUOTATION_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderQuotationReport, parameters, JasperReportNameConstant.ORDER_QUOTATION_REPORT);
	}

	/**
	 * Performs the calculation of the total amount, including functions, tax, discount, and round-off,
	 * for the provided order quotation request.
	 * 
	 * <p>The method iterates through each function in the order quotation request, calculating the total 
	 * based on the function's person count, rate, and extra value. It then applies tax if specified, 
	 * adjusting for IGST or SGST/CGST based on the customer's GST number. The discount and round-off 
	 * values are also incorporated into the final total. The calculated grand total is set in the 
	 * {@link OrderQuotationRequestDto} object.</p>
	 * 
	 * @param orderQuotationRequestDto the order quotation request containing the functions and other relevant data
	 * @param customerContactId the ID of the customer contact, used to check for GST number for tax calculation
	 */
	private void doCalculation(OrderQuotationRequestDto orderQuotationRequestDto, Long customerContactId) {
		Double functionTotal = 0d;
		Double total;
		boolean isRoughEstimation = orderQuotationRequestDto.getIsRoughEstimation(); // Check if it's a rough estimation
		if (isRoughEstimation) {
			int sumBeforeHyphen = 0;
			int sumAfterHyphen = 0;
			// Loop through functions to calculate rough estimation
			for (OrderQuotationFunctionRequestDto function : orderQuotationRequestDto.getFunctions()) {
				String amountValue = function.getAmount(); // Get the range (e.g., "100-200")
				if (amountValue != null) {
					amountValue = amountValue.trim(); // Remove unnecessary spaces
					if (amountValue.matches("\\d+-\\d+")) { // Case: "100-200"
						String[] parts = amountValue.split("-");
						int before = Integer.parseInt(parts[0]);
						int after = Integer.parseInt(parts[1]);
						sumBeforeHyphen += before;
						sumAfterHyphen += after;
					} else if (amountValue.matches("\\d+")) { // Case: "200" (Single Value)
						int value = Integer.parseInt(amountValue);
						sumAfterHyphen += value; // Add single value to after-hyphen sum
					}
				}
			}
			// Store the range as a String in grandTotal
			String grandTotal = (sumBeforeHyphen > 0) ? (sumBeforeHyphen + "-" + sumAfterHyphen) : String.valueOf(sumAfterHyphen);
			orderQuotationRequestDto.setGrandTotal(grandTotal);
		} else {
			for (OrderQuotationFunctionRequestDto function : orderQuotationRequestDto.getFunctions()) {
				functionTotal += (NumberUtils.extractValue(function.getPerson()).intValue() * NumberUtils.extractValue(function.getRate()).doubleValue()) + (NumberUtils.extractValue(function.getExtra()).doubleValue() * NumberUtils.extractValue(function.getRate()).doubleValue());
			}
			total = functionTotal;

			// If tax is present, add required taxes to the total amount
			if (orderQuotationRequestDto.getTaxMasterId() != null) {
				List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
				boolean isIgstRequired = contactRepository.isGstNumberSame(customerContactId);
				Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), orderQuotationRequestDto.getTaxMasterId())).findAny();
				if (isIgstRequired && taxMaster.isPresent()) {
					total += NumberUtils.extractValue(taxMaster.get().getIgst()).doubleValue() * functionTotal / 100.0;
				} else {
					total += (NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getSgst() : 0).doubleValue() * functionTotal / 100.0) + (NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getCgst() : 0).doubleValue() * functionTotal / 100.0);
				}
			}
			total -= NumberUtils.extractValue(orderQuotationRequestDto.getDiscount()).doubleValue();
			total += NumberUtils.extractValue(orderQuotationRequestDto.getRoundOff()).doubleValue();
			orderQuotationRequestDto.setGrandTotal(String.valueOf(total)); // Store as a string
		}
	}

	/**
	 * Sets the company-related information in the provided report parameters.
	 * 
	 * <p>The method fetches the company preferences from the database using the provided language type and 
	 * populates the parameters map with the company's name, address, email, mobile number, and GST number. 
	 * If any value is missing, it is set as an empty string.</p>
	 * 
	 * @param parameters a map to hold the report parameters, where company data will be set
	 * @param langType the language type used to fetch the correct company preferences
	 */
	private void setCompanyData(Map<String, Object> parameters, Integer langType, String reportName) {
		CompanyPreferencesForReportDto companyPreferences = companyPreferencesNativeQueryService.find(DataUtils.getLangType(langType));
		ReportCompanyDetailRightsDto companyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
		if (Objects.nonNull(companyPreferences)) {
			if(Boolean.TRUE.equals(companyDetailRightsDto.getCompanyName())) {
				parameters.put(COMPANY_NAME, Objects.nonNull(companyPreferences.getName()) ? companyPreferences.getName() : "");
			}
			if(Boolean.TRUE.equals(companyDetailRightsDto.getCompanyAddress())) {
				parameters.put(COMPANY_ADDRESS, Objects.nonNull(companyPreferences.getAddress()) ? companyPreferences.getAddress() : "");
			}
			if(Boolean.TRUE.equals(companyDetailRightsDto.getCompanyEmail())) {
				parameters.put(COMPANY_EMAIL, Objects.nonNull(companyPreferences.getEmail()) ? companyPreferences.getEmail() : "");
			}
			if(Boolean.TRUE.equals(companyDetailRightsDto.getCompanyMobileNumber())) {
				parameters.put(COMPANY_MOBILE_NUMBER, Objects.nonNull(companyPreferences.getMobileNumber()) ? companyPreferences.getMobileNumber() : "");
				parameters.put(COMPANY_OFFICE_NUMBER, Objects.nonNull(companyPreferences.getOfficeNumber()) ? companyPreferences.getOfficeNumber() : "");
			}
			parameters.put(COMPANY_GST_NO, Objects.nonNull(companyPreferences.getGstNo()) ? companyPreferences.getGstNo() : "");
		}
	}

}