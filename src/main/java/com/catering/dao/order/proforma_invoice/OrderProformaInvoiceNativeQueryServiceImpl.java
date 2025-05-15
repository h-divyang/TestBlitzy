package com.catering.dao.order.proforma_invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.catering.bean.ErrorGenerator;
import com.catering.bean.FileBean;
import com.catering.constant.FieldConstants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dao.order.BookOrderNativeQueryService;
import com.catering.dao.order.invoice.OrderInvoiceNativeQueryDao;
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dto.tenant.request.OrderInvoiceCommonDtoForReportDto;
import com.catering.dto.tenant.request.OrderInvoiceReportDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceFunctionRequestDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceFunctionResponseDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceRequestDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceResponseDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.model.tenant.OrderProformaInvoiceModel;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.OrderProformaInvoiceRepository;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.JasperReportService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.NumberUtils;
import com.catering.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of {@link OrderProformaInvoiceNativeQueryService} that provides business logic for managing
 * Proforma Invoice data, associated operations, and report generation.
 * This service interacts with multiple data sources, repositories, and utilities to handle Proforma Invoice-related functionality.
 *
 * <p>Dependencies are injected using constructor-based dependency injection, facilitated by the 
 * {@link RequiredArgsConstructor} annotation.</p>
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderProformaInvoiceNativeQueryServiceImpl implements OrderProformaInvoiceNativeQueryService {

	/**
	 * DAO for performing database operations related to Proforma Invoices.
	 */
	OrderProformaInvoiceNativeQueryDao orderProformaInvoiceNativeQueryDao;

	/**
	 * Service for mapping entities and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for Proforma Invoice entity operations.
	 */
	OrderProformaInvoiceRepository orderProformaInvoiceRepository;

	/**
	 * Service for handling and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for generating and managing JasperReports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service for querying menu preparation-related data for reports.
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/**
	 * Repository for managing tax master data.
	 */
	TaxMasterRepository taxMasterRepository;

	/**
	 * Repository for managing contact-related data.
	 */
	ContactRepository contactRepository;

	/**
	 * DAO for order invoice operations, used for additional queries related to invoices.
	 */
	OrderInvoiceNativeQueryDao orderInvoiceNativeQueryDao;

	/**
	 * Repository for managing company preferences and settings.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/**
	 * Service for querying book order-related data.
	 */
	BookOrderNativeQueryService bookOrderNativeQueryService;

	/**
	 * Service interface for fetching company preferences related to report generation.
	 * It retrieves company information such as name, address, and contact details for use in reports.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderProformaInvoiceResponseDto findOrderProformaInvoiceByOrderId(Long orderId) {
		OrderProformaInvoiceResponseDto orderProformaInvoice = orderProformaInvoiceNativeQueryDao.findOrderProformaInvoiceByOrderId(orderId);
		if (companyPreferencesRepository.findSubscriptionId() == 2) {
			orderProformaInvoice.setAdvancePayment(orderProformaInvoice.getAdvancePayment() + orderProformaInvoiceRepository.getAdvancePayment(orderId));
		}
		orderProformaInvoice.setFunctions(orderProformaInvoiceNativeQueryDao.findOrderProformaInvoiceFunctionByOrderId(orderId));
		orderProformaInvoice.setFunctionTotal(Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(Objects.nonNull(orderProformaInvoice.getFunctions()) ? orderProformaInvoice.getFunctions().stream().filter(orderInvoiceFunction -> Objects.nonNull(orderInvoiceFunction.getAmount())).map(OrderProformaInvoiceFunctionResponseDto::getAmount).reduce(0.0, (accumulator, element) -> accumulator + element) : 0)));
		return orderProformaInvoice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderProformaInvoiceRequestDto createAndUpdate(OrderProformaInvoiceRequestDto orderProformaInvoiceRequestDto, Long customerContactId) {
		doCalculation(orderProformaInvoiceRequestDto, customerContactId);
		orderProformaInvoiceRequestDto.setIsActive(true);
		ErrorGenerator errors = ErrorGenerator.builder();
		boolean billNumberExists = Objects.nonNull(orderProformaInvoiceRequestDto.getId())
				? orderProformaInvoiceRepository.existsByBillNumberAndIdNot(orderProformaInvoiceRequestDto.getBillNumber(), orderProformaInvoiceRequestDto.getId())
				: orderProformaInvoiceRepository.existsByBillNumber(orderProformaInvoiceRequestDto.getBillNumber());
		if (billNumberExists) {
			errors.putError(FieldConstants.INVOICE_FILED_BILL_NUMBER, messageService.getMessage(MessagesConstant.ALREADY_EXITS_FIELD, MessagesFieldConstants.COMMON_FIELD_BILL_NUMBER));
		}
		if (Objects.isNull(orderProformaInvoiceRequestDto.getBillDate())) {
			orderProformaInvoiceRequestDto.setBillDate(LocalDate.now());
		}
		if (billNumberExists) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		OrderProformaInvoiceModel orderProformaInvoiceModel = modelMapperService.convertEntityAndDto(orderProformaInvoiceRequestDto, OrderProformaInvoiceModel.class);
		orderProformaInvoiceModel.getFunctions().forEach(function -> function.setOrderProformaInvoice(orderProformaInvoiceModel));
		DataUtils.setAuditFields(orderProformaInvoiceRepository, orderProformaInvoiceRequestDto.getId(), orderProformaInvoiceModel);
		OrderProformaInvoiceModel orderProformaInvoiceModelResponse = orderProformaInvoiceRepository.save(orderProformaInvoiceModel);
		return modelMapperService.convertEntityAndDto(orderProformaInvoiceModelResponse, OrderProformaInvoiceRequestDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderProformaInvoiceReport(Long orderId, Long customerContactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.OrderReport.QuotationReport.class, StringUtils.dotSeparated(JasperParameterConstants.ORDER_REPORT, JasperParameterConstants.OrderReport.QUOTATION_REPORT));
		bookOrderNativeQueryService.setCommonParameters(parameters, langType, reportName);
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		OrderInvoiceCommonDtoForReportDto orderProformaInvoiceCommonDtoForReportDto = orderInvoiceNativeQueryDao.findCommonOrderInvoiceDataForReportByOrderId(orderId, langType);
		if (Objects.nonNull(orderProformaInvoiceCommonDtoForReportDto)) {
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, orderProformaInvoiceCommonDtoForReportDto.getCustomerName());
			parameters.put(ReportParameterConstants.VENUE_VALUE, orderProformaInvoiceCommonDtoForReportDto.getVenue());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, orderProformaInvoiceCommonDtoForReportDto.getCustomerNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_GST_VALUE, orderProformaInvoiceCommonDtoForReportDto.getCustomerGSTNumber());
		}

		OrderProformaInvoiceResponseDto orderProformaInvoice = orderProformaInvoiceNativeQueryDao.findOrderProformaInvoiceByOrderId(orderId);
		orderProformaInvoice.setFunctions(orderProformaInvoiceNativeQueryDao.findOrderProformaInvoiceFunctionByOrderId(orderId));
		orderProformaInvoice.setFunctionTotal(Double.valueOf(NumberUtils.roundOffTwoDecimalPlaces(Objects.nonNull(orderProformaInvoice.getFunctions()) ? orderProformaInvoice.getFunctions().stream().filter(orderQuotationFunction -> Objects.nonNull(orderQuotationFunction.getAmount())).map(OrderProformaInvoiceFunctionResponseDto::getAmount).reduce(0.0, (accumulator, element) -> accumulator + element) : 0)));
		// If tax is present for particular order invoice 
		if (orderProformaInvoice.getTaxMasterId() > 0) {
			List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
			boolean isIgstRequired = contactRepository.isGstNumberSame(customerContactId);
			Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), orderProformaInvoice.getTaxMasterId())).findAny();
			if (taxMaster.isPresent()) {
				bookOrderNativeQueryService.setTaxParameters(taxMaster.get(), isIgstRequired, orderProformaInvoice.getFunctionTotal(), parameters);
			}
		} else {
			bookOrderNativeQueryService.setParameterAsZero(parameters);
		}
		parameters.put(ReportParameterConstants.SUBTOTAL, orderProformaInvoice.getFunctionTotal());
		parameters.put(ReportParameterConstants.DISCOUNT, orderProformaInvoice.getDiscount());
		parameters.put(ReportParameterConstants.GRAND_TOTAL, orderProformaInvoiceRepository.existsByOrderId(orderId) ? orderProformaInvoice.getGrandTotal() : orderProformaInvoice.getFunctionTotal());
		parameters.put(ReportParameterConstants.ADVANCE_PAYMENT, orderProformaInvoice.getAdvancePayment());
		parameters.put(ReportParameterConstants.ROUND_OFF, orderProformaInvoice.getRoundOff());
		parameters.put(ReportParameterConstants.TOTAL_VALUE, orderProformaInvoice.getTotal());
		parameters.put(ReportParameterConstants.REMARK, orderProformaInvoice.getRemark());
		parameters.put(ReportParameterConstants.BILL_DATE_VALUE, orderProformaInvoice.getBillDate());
		parameters.put(ReportParameterConstants.BILL_NUMBER_VALUE, orderProformaInvoice.getBillNumber());
		parameters.put(ReportParameterConstants.COMPANY_REGISTERED_ADDRESS, orderProformaInvoice.getCompanyRegisteredAddress());
		parameters.put(ReportParameterConstants.CONTACT_PERSON_MOBILE, orderProformaInvoice.getContactPersonMobileNo());
		parameters.put(ReportParameterConstants.CONTACT_PERSON_NAME, orderProformaInvoice.getContactPersonName());
		parameters.put(ReportParameterConstants.PO_NUMBER, orderProformaInvoice.getPoNumber());

		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<OrderInvoiceReportDto> orderProformaInvoiceReport = orderProformaInvoiceNativeQueryDao.generateOrderProformaInvoiceReport(orderId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ORDER_PROFORMA_INVOICE_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderProformaInvoiceReport, parameters, JasperReportNameConstant.ORDER_PROFORMA_INVOICE_REPORT);
	}

	/**
	 * Performs necessary calculations for the given order proforma invoice request.
	 *
	 * @param orderProformaInvoiceRequestDto The order invoice request data for which calculations are to be performed.
	 */
	private void doCalculation(OrderProformaInvoiceRequestDto orderProformaInvoiceRequestDto, Long customerContactId) {
		Double functionTotal = 0d;
		Double total;
		for (OrderProformaInvoiceFunctionRequestDto function : orderProformaInvoiceRequestDto.getFunctions()) {
			functionTotal += (NumberUtils.extractValue(function.getPerson()).intValue() * NumberUtils.extractValue(function.getRate()).doubleValue()) + (NumberUtils.extractValue(function.getExtra()).doubleValue() * NumberUtils.extractValue(function.getRate()).doubleValue());
		}
		total = functionTotal;
		if (orderProformaInvoiceRequestDto.getTaxMasterId() != null) {
			List<TaxMasterDto> taxMasterDto = modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAll(), TaxMasterDto.class);
			boolean isIgstRequired = contactRepository.isGstNumberSame(customerContactId);
			Optional<TaxMasterDto> taxMaster = taxMasterDto.stream().filter(tax -> Objects.equals(tax.getId(), orderProformaInvoiceRequestDto.getTaxMasterId())).findAny();
			if (isIgstRequired) {
				total += NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getIgst() : 0).doubleValue() * functionTotal / 100.0;
			} else {
				total += (NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getSgst(): 0).doubleValue() * functionTotal / 100.0) + (NumberUtils.extractValue(taxMaster.isPresent() ? taxMaster.get().getCgst() : 0).doubleValue() * functionTotal / 100.0);
			}
		}

		total -= NumberUtils.extractValue(orderProformaInvoiceRequestDto.getDiscount()).doubleValue();
		total += NumberUtils.extractValue(orderProformaInvoiceRequestDto.getRoundOff()).doubleValue();
		orderProformaInvoiceRequestDto.setGrandTotal(total);
	}

}