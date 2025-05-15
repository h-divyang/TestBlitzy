package com.catering.dao.order.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.ErrorGenerator;
import com.catering.bean.FileBean;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dao.order.BookOrderNativeQueryService;
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dto.tenant.request.OrderInvoiceCommonDtoForReportDto;
import com.catering.dto.tenant.request.OrderInvoiceFunctionRequestDto;
import com.catering.dto.tenant.request.OrderInvoiceFunctionResponseDto;
import com.catering.dto.tenant.request.OrderInvoiceReportDto;
import com.catering.dto.tenant.request.OrderInvoiceRequestDto;
import com.catering.dto.tenant.request.OrderInvoiceResponseDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.model.tenant.OrderInvoiceModel;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.OrderInvoiceRepository;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.JasperReportService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.NumberToStringUtils;
import com.catering.util.NumberUtils;
import com.catering.util.StringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation class for native queries related to order invoices.
 * Provides methods for retrieving, creating or updating, and generating reports for order invoices.
 *
 * This service includes functionality for calculating invoice details, setting up report parameters,
 * and utilizing JasperReports for generating PDF reports.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderInvoiceNativeQueryServiceImpl implements OrderInvoiceNativeQueryService {

	/** 
	 * DAO for executing native queries related to order invoices.
	 */
	OrderInvoiceNativeQueryDao orderInvoiceNativeQueryDao;

	/** 
	 * Service for mapping data between different object models, often used for DTO conversion.
	 */
	ModelMapperService modelMapperService;

	/** 
	 * Repository interface for performing CRUD operations on order invoice data.
	 */
	OrderInvoiceRepository orderInvoiceRepository;

	/** 
	 * Service for generating reports using JasperReports framework, including report customization and parameter management.
	 */
	JasperReportService jasperReportService;

	/** 
	 * Service for managing and retrieving localized messages and notifications.
	 */
	MessageService messageService;

	/** 
	 * Service for handling application exceptions and providing standardized error responses.
	 */
	ExceptionService exceptionService;

	/** 
	 * Service for generating reports related to menu preparation using custom queries.
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/** 
	 * Repository for managing tax-related data, including CRUD operations on tax entities.
	 */
	TaxMasterRepository taxMasterRepository;

	/** 
	 * Repository for managing contact-related data, including CRUD operations on contact entities.
	 */
	ContactRepository contactRepository;

	/** 
	 * Repository for accessing and managing company preference data.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/** 
	 * Service for executing custom native queries and managing company preferences data.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/** 
	 * Service for managing book order operations and related data using native queries.
	 */
	BookOrderNativeQueryService bookOrderNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderInvoiceResponseDto findOrderInvoiceByOrderId(Long orderId) {
		OrderInvoiceResponseDto orderInvoice = orderInvoiceNativeQueryDao.findOrderInvoiceByOrderId(orderId);
		if (companyPreferencesRepository.findSubscriptionId() == 2) {
			orderInvoice.setAdvancePayment(orderInvoice.getAdvancePayment() + orderInvoiceRepository.getAdvancePayment(orderId));
		}
		orderInvoice.setFunctions(orderInvoiceNativeQueryDao.findOrderInvoiceFunctionByOrderId(orderId));
		orderInvoice.setFunctionTotal(Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(Objects.nonNull(orderInvoice.getFunctions()) ? orderInvoice.getFunctions().stream().filter(orderInvoiceFunction -> Objects.nonNull(orderInvoiceFunction.getAmount())).map(OrderInvoiceFunctionResponseDto::getAmount).reduce(0.0, (accumulator, element) -> accumulator + element) : 0)));
		return orderInvoice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderInvoiceRequestDto createAndUpdate(OrderInvoiceRequestDto orderInvoiceRequestDto, Long customerContactId) {
		doCalculation(orderInvoiceRequestDto, customerContactId);
		orderInvoiceRequestDto.setIsActive(true);
		ErrorGenerator errors = ErrorGenerator.builder();
		boolean billNumberExists = Objects.nonNull(orderInvoiceRequestDto.getId())
				? orderInvoiceRepository.existsByBillNumberAndIdNot(orderInvoiceRequestDto.getBillNumber(), orderInvoiceRequestDto.getId())
				: orderInvoiceRepository.existsByBillNumber(orderInvoiceRequestDto.getBillNumber());
		if (billNumberExists) {
			errors.putError(FieldConstants.INVOICE_FILED_BILL_NUMBER, messageService.getMessage(MessagesConstant.ALREADY_EXITS_FIELD, MessagesFieldConstants.COMMON_FIELD_BILL_NUMBER));
		}
		if (Objects.isNull(orderInvoiceRequestDto.getBillDate())) {
			orderInvoiceRequestDto.setBillDate(LocalDate.now());
		}
		if (billNumberExists) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		OrderInvoiceModel orderInvoiceModel = modelMapperService.convertEntityAndDto(orderInvoiceRequestDto, OrderInvoiceModel.class);
		orderInvoiceModel.getFunctions().forEach(function -> function.setOrderInvoice(orderInvoiceModel));
		DataUtils.setAuditFields(orderInvoiceRepository, orderInvoiceRequestDto.getId(), orderInvoiceModel);
		OrderInvoiceModel orderInvoiceModelResponse = orderInvoiceRepository.save(orderInvoiceModel);
		return modelMapperService.convertEntityAndDto(orderInvoiceModelResponse, OrderInvoiceRequestDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderInvoiceReport(Long orderId, Long customerContactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Object tenant = request.getAttribute(Constants.TENANT);
		Map<String, Object> parameters;
		if (Objects.equals(tenant, Constants.SHYAM_CATERERS_SURAT_TENANT) || Objects.equals(tenant, Constants.SHYAM_CATERERS_AMRELI_TENANT)) {
			parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.CustomMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.CUSTOM_MENU_REPORT));
		} else {
			parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.OrderReport.QuotationReport.class, StringUtils.dotSeparated(JasperParameterConstants.ORDER_REPORT, JasperParameterConstants.OrderReport.QUOTATION_REPORT));
		}
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		NumberToStringUtils numberToStringUtils = new NumberToStringUtils();
		bookOrderNativeQueryService.setCommonParameters(parameters, langType, reportName);

		OrderInvoiceCommonDtoForReportDto orderInvoiceCommonDtoForReportDto = orderInvoiceNativeQueryDao.findCommonOrderInvoiceDataForReportByOrderId(orderId, langType);
		if (Objects.nonNull(orderInvoiceCommonDtoForReportDto)) {
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, orderInvoiceCommonDtoForReportDto.getCustomerName());
			parameters.put(ReportParameterConstants.VENUE_VALUE, orderInvoiceCommonDtoForReportDto.getVenue());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, orderInvoiceCommonDtoForReportDto.getCustomerNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_GST_VALUE, orderInvoiceCommonDtoForReportDto.getCustomerGSTNumber());
		}

		OrderInvoiceResponseDto orderInvoice = orderInvoiceNativeQueryDao.findOrderInvoiceByOrderId(orderId);
		orderInvoice.setFunctions(orderInvoiceNativeQueryDao.findOrderInvoiceFunctionByOrderId(orderId));
		orderInvoice.setFunctionTotal(Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(Objects.nonNull(orderInvoice.getFunctions()) ? orderInvoice.getFunctions().stream().filter(orderQuotationFunction -> Objects.nonNull(orderQuotationFunction.getAmount())).map(OrderInvoiceFunctionResponseDto::getAmount).reduce(0.0, (accumulator, element) -> accumulator + element) : 0)));
		// If tax is present for particular order invoice 
		if (orderInvoice.getTaxMasterId() > 0) {
			List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
			boolean isIgstRequired = contactRepository.isGstNumberSame(customerContactId);
			Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), orderInvoice.getTaxMasterId())).findAny();
			if (taxMaster.isPresent()) {
				bookOrderNativeQueryService.setTaxParameters(taxMaster.get(), isIgstRequired, orderInvoice.getFunctionTotal(), parameters);
			}
		} else {
			bookOrderNativeQueryService.setParameterAsZero(parameters);
		}
		parameters.put(ReportParameterConstants.SUBTOTAL, orderInvoice.getFunctionTotal());
		parameters.put(ReportParameterConstants.DISCOUNT, orderInvoice.getDiscount());
		parameters.put(ReportParameterConstants.GRAND_TOTAL, orderInvoiceRepository.existsByOrderId(orderId) ? orderInvoice.getGrandTotal() : orderInvoice.getFunctionTotal());
		parameters.put(ReportParameterConstants.ADVANCE_PAYMENT, orderInvoice.getAdvancePayment());
		parameters.put(ReportParameterConstants.ROUND_OFF, orderInvoice.getRoundOff());
		parameters.put(ReportParameterConstants.TOTAL_VALUE, orderInvoice.getTotal());
		parameters.put(ReportParameterConstants.COMPANY_REGISTERED_ADDRESS, orderInvoice.getCompanyRegisteredAddress());
		parameters.put(ReportParameterConstants.CONTACT_PERSON_MOBILE, orderInvoice.getContactPersonMobileNo());
		parameters.put(ReportParameterConstants.CONTACT_PERSON_NAME, orderInvoice.getContactPersonName());
		parameters.put(ReportParameterConstants.PO_NUMBER, orderInvoice.getPoNumber());
		if (Objects.equals(tenant, Constants.SHREE_RAJ_CATERING)) {
			parameters.put(ReportParameterConstants.RUPEES_IN_WORDS_VALUE, numberToStringUtils.convertToWords(orderInvoice.getTotal().longValue()));	
		}
		parameters.put(ReportParameterConstants.REMARK, orderInvoice.getRemark());
		parameters.put(ReportParameterConstants.BILL_DATE_VALUE, orderInvoice.getBillDate());
		parameters.put(ReportParameterConstants.BILL_NUMBER_VALUE, orderInvoice.getBillNumber());

		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<OrderInvoiceReportDto> orderInvoiceReport = orderInvoiceNativeQueryDao.generateOrderInvoiceReport(orderId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ORDER_INVOICE_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderInvoiceReport, parameters, JasperReportNameConstant.ORDER_INVOICE_REPORT);
	}

	/**
	 * Performs necessary calculations for the given order invoice request.
	 *
	 * @param orderInvoiceRequestDto The order invoice request data for which calculations are to be performed.
	 */
	private void doCalculation(OrderInvoiceRequestDto orderInvoiceRequestDto, Long customerContactId) {
		Double functionTotal = 0d;
		Double total;
		for (OrderInvoiceFunctionRequestDto function : orderInvoiceRequestDto.getFunctions()) {
			functionTotal += (NumberUtils.extractValue(function.getPerson()).intValue() * NumberUtils.extractValue(function.getRate()).doubleValue()) + (NumberUtils.extractValue(function.getExtra()).doubleValue() * NumberUtils.extractValue(function.getRate()).doubleValue());
		}
		total = functionTotal;
		if (orderInvoiceRequestDto.getTaxMasterId() != null) {
			List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
			boolean isIgstRequired = contactRepository.isGstNumberSame(customerContactId);
			Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), orderInvoiceRequestDto.getTaxMasterId())).findAny();
			if (isIgstRequired) {
				total += NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getIgst() : 0).doubleValue() * functionTotal / 100.0;
			} else {
				total += (NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getSgst() : 0).doubleValue() * functionTotal / 100.0) + (NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getCgst() : 0).doubleValue() * functionTotal / 100.0);
			}
		}

		total -= NumberUtils.extractValue(orderInvoiceRequestDto.getDiscount()).doubleValue();
		total += NumberUtils.extractValue(orderInvoiceRequestDto.getRoundOff()).doubleValue();
		orderInvoiceRequestDto.setGrandTotal(total);
	}

}