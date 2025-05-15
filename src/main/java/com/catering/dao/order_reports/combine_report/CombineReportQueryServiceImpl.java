package com.catering.dao.order_reports.combine_report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.MessagesConstant;
import com.catering.constant.ReportNameConstant;
import com.catering.dao.order_reports.admin_reports.AdminReportQueryService;
import com.catering.dao.order_reports.labour_and_agency.LabourAndAgencyReportQueryService;
import com.catering.dao.order_reports.menu_allocation.MenuAllocationReportQueryService;
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dao.order_reports.order_general_fix_and_crockery_allocation.OrderGeneralFixAndCrockeryAllocationReportQueryService;
import com.catering.dto.tenant.request.ChefOrOutsideLabourReportParams;
import com.catering.dto.tenant.request.CombineReportRequestParmDto;
import com.catering.dto.tenant.request.LabourReportParams;
import com.catering.dto.tenant.request.CombineReportDto;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanySettingService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CombineReportQueryServiceImpl implements CombineReportQueryService {

	/**
	 * Service for generating admin reports.
	 */
	AdminReportQueryService adminReportQueryService;

	/**
	 * Service for managing company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * Service for generating custom exception.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for generating labour and agency reports.
	 */
	LabourAndAgencyReportQueryService labourAndAgencyReportQueryService;

	/**
	 * Service for message
	 */
	MessageService messageService;

	/**
	 * Service for generating menu preparation reports.
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/**
	 * Service for generating menu allocation reports.
	 */
	MenuAllocationReportQueryService menuAllocationReportQueryService;

	/**
	 * Service for generating order general fix and crockery allocation reports.
	 */
	OrderGeneralFixAndCrockeryAllocationReportQueryService orderGeneralFixAndCrockeryAllocationReportQueryService;

	Logger logger = LoggerFactory.getLogger(CombineReportQueryServiceImpl.class);

	public FileBean generateCombineReport(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		if (requestParm.getSelectedReportList().isEmpty()) {
			return null;
		}
		// Call different report-generation methods based on need
		fileBeans.addAll(menuPreparation(orderId, requestParm, request));
		fileBeans.addAll(menuAllocation(orderId, requestParm, request));
		fileBeans.addAll(rawMaterialAllocation(orderId, requestParm, request));
		fileBeans.addAll(labourAndAgency(orderId, requestParm, request));
		fileBeans.addAll(generalFixAndCrockery(orderId, requestParm, request));
		fileBeans.addAll(adminHub(orderId, requestParm, request));
		// Remove null entries (if no matching report was found)
		fileBeans.removeIf(Objects::isNull);

		return mergePdfReports(fileBeans);
	}

	private List<FileBean> menuPreparation(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		List<CombineReportDto> categoryList = requestParm.getSelectedReportList().stream().filter(value -> value.getReportMaster().getReportCategory().getId() == 1).toList();
		for (CombineReportDto reportDto : categoryList) {
			String reportName = reportDto.getReportMaster().getReportName();
			FileBean fileBean = getMenuPreparationReport(orderId, requestParm, request, reportName);

			if (fileBean != null)
				fileBeans.add(fileBean); 
		}
		return fileBeans;
	}

	private FileBean getMenuPreparationReport(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request, String reportName) {
		Long[] functionId = requestParm.getFunctionId();
		Integer langType = requestParm.getLangType();
		String langCode = requestParm.getLangCode();
		String defaultLang = requestParm.getDefaultLang();
		String preferLang = requestParm.getPreferLang();

		return switch (reportName) {
		case ReportNameConstant.CUSTOM_MENU_REPORT -> 
			menuPreparationReportQueryService.generateCustomMenuPreparation(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.SIMPLE_MENU_REPORT -> 
			menuPreparationReportQueryService.generateSimpleMenuReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.EXCLUSIVE_MENU_REPORT -> 
			menuPreparationReportQueryService.generateExclusiveMenuReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.SLOGAN_MENU_REPORT -> 
			menuPreparationReportQueryService.generateSloganMenuReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.PREMIUM_IMAGE_MENU_REPORT -> 
			menuPreparationReportQueryService.generatePremiumImageMenuReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.IMAGE_REPORT_WITH_MENU -> 
			menuPreparationReportQueryService.generateImageMenuReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.IMAGE_AND_SLOGAN_MENU_REPORT -> 
			menuPreparationReportQueryService.generateMenuWithImageAndSloganReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.IMAGE_MENU_CATEGORY_REPORT -> 
			menuPreparationReportQueryService.generateImageMenuCategoryReport(orderId, functionId, langType, langCode, reportName, request);

		case ReportNameConstant.TWO_LANGUAGE_MENU_REPORT -> 
			menuPreparationReportQueryService.generateTwoLanguageMenuReport(orderId, functionId, langType, defaultLang, preferLang, langCode, reportName, request);

		case ReportNameConstant.INSTRUCTION_MENU_REPORT -> 
			menuPreparationReportQueryService.generateInstructionMenuReport(orderId, functionId, langType, langCode, reportName, request);
		
		case ReportNameConstant.MANAGER_MENU_REPORT -> 
			menuPreparationReportQueryService.generateManagerMenuReport(orderId, functionId, langType, langCode, reportName, request);

		default -> null;
		};
	}

	private List<FileBean> menuAllocation(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		List<CombineReportDto> categoryList = requestParm.getSelectedReportList().stream().filter(value -> value.getReportMaster().getReportCategory().getId() == 2).toList();
		for (CombineReportDto reportDto : categoryList) {
			String reportName = reportDto.getReportMaster().getReportName();
			FileBean fileBean = getMenuAllocationReport(orderId, requestParm, request, reportName);

			if (fileBean != null)
				fileBeans.add(fileBean); // Return first matched report
		}
		return fileBeans;
	}

	private FileBean getMenuAllocationReport(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request, String reportName) {
		Integer langType = requestParm.getLangType();
		String langCode = requestParm.getLangCode();
		Long[] functionId = requestParm.getFunctionId();

		return switch (reportName) {
		case ReportNameConstant.MENU_WITH_QUANTITY_REPORT -> 
			menuAllocationReportQueryService.generateMenuWithAndWithOutQuantityReport(
				orderId, functionId, langType, langCode,
				ReportNameConstant.MENU_WITH_QUANTITY_REPORT, request,
				JasperReportNameConstant.MENU_ALLOCATION_REPORT_MENU_WITH_QUANTITY_REPORT);

		case ReportNameConstant.MENU_WITH_OUT_QUANTITY_REPORT -> 
			menuAllocationReportQueryService.generateMenuWithAndWithOutQuantityReport(
				orderId, functionId, langType, langCode,
				ReportNameConstant.MENU_WITH_OUT_QUANTITY_REPORT, request,
				JasperReportNameConstant.MENU_ALLOCATION_REPORT_MENU_OUT_WITH_QUANTITY_REPORT);

		default -> null;
		};
	}

	private List<FileBean> rawMaterialAllocation(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		List<CombineReportDto> categoryList = requestParm.getSelectedReportList().stream()
				.filter(value -> value.getReportMaster().getReportCategory().getId() == 3 || value.getReportMaster().getReportCategory().getId() == 4)
				.sorted(Comparator.comparing(value -> value.getReportMaster().getId())).toList();
		for (CombineReportDto reportDto : categoryList) {
			String reportName = reportDto.getReportMaster().getReportName();
			Object result = getRawMaterialAllocationReports(orderId, requestParm, request, reportName);
			if (result instanceof FileBean) {
				FileBean fileBean = (FileBean) result;
				fileBeans.add(fileBean);
			}
		}
		return fileBeans;
	}

	private Object getRawMaterialAllocationReports(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request, String reportName) {
		Integer langType = requestParm.getLangType();
		String langCode = requestParm.getLangCode();
		Long[] functionId = requestParm.getFunctionId();
		Long[] rawMaterialCategoryId = requestParm.getRawMaterialCategoryId();
		Long[] generalFixRawMaterialCategoryId = requestParm.getGeneralFixRawMaterialCategoryId();
		Boolean isDateTime = requestParm.getIsDateTime();
		LocalDateTime orderDate = requestParm.getOrderDate();
		boolean isWithQuantity = requestParm.isWithQuantity();
		Long count = requestParm.getCount();
		Long[] supplierWiseRawMaterialContact = requestParm.getSupplierWiseContactId();
		Long[] typesOfData = requestParm.getTypesOfData();
		Long[] chefLabourAgencyType = requestParm.getAgencyType();
		Long[] chefLabourAgencyNameId = requestParm.getAgencyNameId();
		Long[] chefLabourMenuItemId = requestParm.getMenuItemId();

		return switch (reportName) {
		case ReportNameConstant.MENU_ITEM_WISE_RAW_MATERIAL_REPORT -> 
			menuAllocationReportQueryService.generateMenuItemWiseRawMaterialReport(orderId, functionId, rawMaterialCategoryId, langType, langCode, reportName, request);

		case ReportNameConstant.DETAILED_RAW_MATERIAL_REPORT -> 
			menuAllocationReportQueryService.generateDetailRawMaterialReport(orderId, isDateTime, orderDate, functionId, orderId, rawMaterialCategoryId, langType, langCode, reportName, request, isWithQuantity, false);

		case ReportNameConstant.TOTAL_RAW_MATERIAL_REPORT -> {
			boolean isDynamic = Boolean.TRUE.equals(companySettingService.getCompannySetting(false).getIsDynamicDesign());
			yield isDynamic
				? menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_DYNAMIC_DESIGN_REPORT, isWithQuantity, false)
				: menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_REPORT, isWithQuantity, false);
		}

		case ReportNameConstant.TOTAL_RAW_MATERIAL_WITH_CATEGORY_REPORT -> 
			menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_WITH_CATEGORY_REPORT, isWithQuantity, false);

		case ReportNameConstant.SUPPLIER_WISE_RAW_MATERIAL_REPORT -> 
			menuAllocationReportQueryService.generateSupplierWiseRawMaterialReport(orderId, langType, langCode, reportName, supplierWiseRawMaterialContact, rawMaterialCategoryId, request);

		case ReportNameConstant.ORDER_FILE_REPORT -> 
			menuAllocationReportQueryService.generateOrderFileReport(orderId, functionId, rawMaterialCategoryId, typesOfData, langType, langCode, reportName, request);

		case ReportNameConstant.TIME_WISE_RAW_MATERIAL_A4_REPORT -> 
			menuAllocationReportQueryService.generateRawMaterialA4Report(orderId, functionId, generalFixRawMaterialCategoryId, langType, langCode, reportName, request, false);

		case ReportNameConstant.TIME_WISE_RAW_MATERIAL_A5_REPORT -> 
			menuAllocationReportQueryService.generateRawMaterialA5Report(orderId, functionId, generalFixRawMaterialCategoryId, langType, langCode, reportName, request, false);

		case ReportNameConstant.TIME_WISE_RAW_MATERIAL_A6_REPORT -> 
			menuAllocationReportQueryService.generateRawMaterialA6Report(orderId, functionId, generalFixRawMaterialCategoryId, langType, langCode, reportName, request, false);

		case ReportNameConstant.CHEF_LABOUR_WISE_RAW_MATERIAL_REPORT -> 
			menuAllocationReportQueryService.generateChefLabourWiseRawMaterialReport(orderId, isDateTime, orderDate, chefLabourAgencyType, chefLabourAgencyNameId, functionId, count, rawMaterialCategoryId, chefLabourMenuItemId, langType, langCode, reportName, request, false);

		case ReportNameConstant.CHEF_LABOUR_SUPPLIER_WISE_RAW_MATERIAL_REPORT -> 
			menuAllocationReportQueryService.generateChefLabourSupplierWiseRawMaterialReport(orderId, isDateTime, orderDate, chefLabourAgencyType, chefLabourAgencyNameId, functionId, count, rawMaterialCategoryId, chefLabourMenuItemId, langType, langCode, reportName, request, false);

		default -> null;
		};
	}

	private List<FileBean> labourAndAgency(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		List<CombineReportDto> categoryList = requestParm.getSelectedReportList().stream().filter(value -> value.getReportMaster().getReportCategory().getId() == 5)
				.sorted(Comparator.comparing(value -> value.getReportMaster().getId())).toList();
		for (CombineReportDto reportDto : categoryList) {
			String reportName = reportDto.getReportMaster().getReportName();
			FileBean fileBean = getLabourAndAgencyReport(orderId, requestParm, request, reportName);

			if (fileBean != null)
				fileBeans.add(fileBean); // Return first matched report
		}
		return fileBeans;
	}

	private FileBean getLabourAndAgencyReport(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request, String reportName) {
		ChefOrOutsideLabourReportParams chefOrOutsideLabourReportParams = new ChefOrOutsideLabourReportParams();
		chefOrOutsideLabourReportParams.setLangType(requestParm.getLangType());
		chefOrOutsideLabourReportParams.setLangCode(requestParm.getLangCode());
		chefOrOutsideLabourReportParams.setFunctionId(requestParm.getFunctionId());

		return switch (reportName) {
		case ReportNameConstant.BOOKING_REPORT -> 
			labourAndAgencyReportQueryService.generateBookingReport(orderId, chefOrOutsideLabourReportParams.getLangType(), chefOrOutsideLabourReportParams.getLangCode(), reportName);

		case ReportNameConstant.CHEF_LABOUR_REPORT, ReportNameConstant.CHEF_LABOUR_CHITHHI_REPORT -> {
			chefOrOutsideLabourReportParams.setContactId(requestParm.getChefLabourSupplier());
			chefOrOutsideLabourReportParams.setMenuItemId(requestParm.getChefLabourMenuItemName());
			yield reportName.equals(ReportNameConstant.CHEF_LABOUR_REPORT)
				? labourAndAgencyReportQueryService.generateChefLabourReport(orderId, chefOrOutsideLabourReportParams, reportName, request)
				: labourAndAgencyReportQueryService.generateChefLabourChithhiReport(orderId, chefOrOutsideLabourReportParams, reportName, request);
		}

		case ReportNameConstant.OUTSIDE_AGENCY_REPORT, ReportNameConstant.OUTSIDE_AGENCY_CHITHHI_REPORT -> {
			chefOrOutsideLabourReportParams.setContactId(requestParm.getOutsideLabourSupplier());
			chefOrOutsideLabourReportParams.setMenuItemId(requestParm.getOutsideLabourMenuItemName());
			yield reportName.equals(ReportNameConstant.OUTSIDE_AGENCY_REPORT)
				? labourAndAgencyReportQueryService.generateOutsideAgencyReport(orderId, chefOrOutsideLabourReportParams, reportName, request)
				: labourAndAgencyReportQueryService.generateOutsideAgencyChithhiReport(orderId, chefOrOutsideLabourReportParams, reportName, request);
		}

		case ReportNameConstant.LABOUR_REPORT, ReportNameConstant.LABOUR_CHITHHI_REPORT -> {
			LabourReportParams labourReportParams = new LabourReportParams();
			labourReportParams.setCurrentDate(LocalDate.now().toString());
			labourReportParams.setFunctionId(requestParm.getFunctionId());
			labourReportParams.setLangCode(requestParm.getLangCode());
			labourReportParams.setLangType(requestParm.getLangType());
			labourReportParams.setSupplierCategoryId(requestParm.getLabourSupplierCategoryId());
			labourReportParams.setSupplierId(requestParm.getLabourSupplierId());
			yield reportName.equals(ReportNameConstant.LABOUR_REPORT)
				? labourAndAgencyReportQueryService.generateLabourReport(orderId, labourReportParams, reportName, request)
				: labourAndAgencyReportQueryService.generateLabourChithhiReport(orderId, labourReportParams, reportName, request);
		}

		default -> null;
		};
	}

	private List<FileBean> generalFixAndCrockery(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		List<CombineReportDto> categoryList = requestParm.getSelectedReportList().stream().filter(value -> value.getReportMaster().getReportCategory().getId() == 6)
				.sorted(Comparator.comparing(value -> value.getReportMaster().getId())).toList();
		for (CombineReportDto reportDto : categoryList) {
			String reportName = reportDto.getReportMaster().getReportName();
			FileBean fileBean = getGeneralFixAndCrockeryReport(orderId, requestParm, request, reportName);

			if (fileBean != null)
				fileBeans.add(fileBean); // Return first matched report
		}
		return fileBeans;
	}

	private FileBean getGeneralFixAndCrockeryReport(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request, String reportName) {
		Integer langType = requestParm.getLangType();
		String langCode = requestParm.getLangCode();

		return switch (reportName) {
		case ReportNameConstant.GENERAL_FIX_WITH_QUANTITY_REPORT ->
			requestParm.isMaxSetting()
				? orderGeneralFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithQuantityReport(orderId, langType, langCode, reportName, request)
				: orderGeneralFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);

		case ReportNameConstant.GENERAL_FIX_WITHOUT_QUANTITY_REPORT ->
			requestParm.isMaxSetting()
				? orderGeneralFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithoutQuantityReport(orderId, langType, langCode, reportName, request)
				: orderGeneralFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithoutQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);

		case ReportNameConstant.CROCKERY_WITH_QUANTITY_REPORT ->
			requestParm.isMaxSetting()
				? orderGeneralFixAndCrockeryAllocationReportQueryService.generateCrockeryWithQuantityReport(orderId, langType, langCode, reportName, request)
				: orderGeneralFixAndCrockeryAllocationReportQueryService.generateCrockeryWithQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);

		case ReportNameConstant.CROCKERY_WITHOUT_QUANTITY_REPORT ->
			requestParm.isMaxSetting()
				? orderGeneralFixAndCrockeryAllocationReportQueryService.generateCrockeryWithoutQuantityReport(orderId, langType, langCode, reportName, request)
				: orderGeneralFixAndCrockeryAllocationReportQueryService.generateCrockeryWithoutQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);

		case ReportNameConstant.KITCHEN_CATEGORY_WITH_QTY_REPORT ->
			requestParm.isMaxSetting()
				? orderGeneralFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithQuantityReport(orderId, langType, langCode, reportName, request)
				: orderGeneralFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);

		case ReportNameConstant.KITCHEN_CATEGORY_WITHOUT_QTY_REPORT ->
			requestParm.isMaxSetting()
				? orderGeneralFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithoutQuantityReport(orderId, langType, langCode, reportName, request)
				: orderGeneralFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);

		default -> null;
		};
	}

	private List<FileBean> adminHub(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request) {
		List<FileBean> fileBeans = new ArrayList<>();
		List<CombineReportDto> categoryList = requestParm.getSelectedReportList().stream().filter(value -> value.getReportMaster().getReportCategory().getId() == 7).toList();
		for (CombineReportDto reportDto : categoryList) {
			String reportName = reportDto.getReportMaster().getReportName();
			FileBean fileBean = getAdminHubReport(orderId, requestParm, request, reportName);

			if (fileBean != null)
				fileBeans.add(fileBean); // Return first matched report
		}
		return fileBeans;
	}

	private FileBean getAdminHubReport(Long orderId, CombineReportRequestParmDto requestParm, HttpServletRequest request, String reportName) {
		Integer langType = requestParm.getLangType();
		String langCode = requestParm.getLangCode();

		if (reportName.equals(ReportNameConstant.DISH_COUNTING_REPORT)) {
			return adminReportQueryService.generateDishCountingReport(orderId, langType, langCode, reportName, request);
		}
		return null;
	}

	/**
	 * Merges a list of PDF reports into a single PDF file.
	 * 
	 * @param pdfFiles the list of PDF files to merge
	 * @return a {@link FileBean} containing the merged PDF as a byte array
	 */
	private FileBean mergePdfReports(List<FileBean> pdfFiles) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			Document document = new Document();
			PdfCopy copy = new PdfCopy(document, outputStream);
			document.open();
			boolean hasValidPages = false;
			byte[] lastPdfBytes = null;

			for (FileBean pdfFile : pdfFiles) {
				if (pdfFile != null && pdfFile.getFile() != null && pdfFile.getFile().length > 0) {
					byte[] fileData = pdfFile.getFile();
					lastPdfBytes = fileData; 

					// Skip PDFs that are completely empty
					if (!isPdfContentEmpty(fileData)) {
						try (InputStream is = new ByteArrayInputStream(fileData)) {
							PdfReader reader = new PdfReader(is);
							int totalPages = reader.getNumberOfPages();
							boolean hasNonBlankPage = false;

							for (int page = 1; page <= totalPages; page++) {
								if (!isPageBlank(reader, page)) {
									copy.addPage(copy.getImportedPage(reader, page));
									hasNonBlankPage = true;
								}
							}

							reader.close();

							if (hasNonBlankPage) {
								hasValidPages = true;
							} else {
								logger.info("Skipping report as all its pages are blank.");
							}
						}
					} else {
						logger.info("Skipping empty PDF report.");
					}
				}
			}

			// If all reports were blank pass the byte code
			if (!hasValidPages) {
				if (lastPdfBytes == null) exceptionService.throwRestException(HttpStatus.NOT_FOUND, messageService.getMessage(MessagesConstant.NOT_EXIST));
				return FileBean.builder().contentType(MediaType.APPLICATION_PDF_VALUE).file(lastPdfBytes).build();
			}

			if (document != null) {
				document.close();
			}

			return FileBean.builder().contentType(MediaType.APPLICATION_PDF_VALUE).file(outputStream.toByteArray()).build();
		} catch (RuntimeException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Checks if a PDF document is empty.
	 * 
	 * @param pdfBytes the byte array representing the PDF document
	 * @return true if the PDF is empty, false otherwise
	 */
	private boolean isPdfContentEmpty(byte[] pdfBytes) {
		PdfReader reader = null;
		try {
			reader = new PdfReader(new ByteArrayInputStream(pdfBytes));
			int totalPages = reader.getNumberOfPages();

			// Check text content for all pages
			for (int page = 1; page <= totalPages; page++) {
				String text = PdfTextExtractor.getTextFromPage(reader, page).trim();
				if (!text.isEmpty()) {
					return false; // Found non-empty text, not an empty PDF
				}
			}
			return true; // No meaningful content found
		} catch (IOException e) {
			e.printStackTrace();
			return true; // Treat as empty if an error occurs
		} finally {
			if (reader != null) {
				reader.close(); // Close PdfReader manually
			}
		}
	}

	/**
	 * Check if page contain any text
	 *
	 * @param reader
	 * @param pageNumber
	 * @return boolean value
	 */
	private boolean isPageBlank(PdfReader reader, int pageNumber) {
		try {
			String pageText = PdfTextExtractor.getTextFromPage(reader, pageNumber).trim();
			return pageText.isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
			return true; // Treat as blank if an error occurs
		}
	}

}