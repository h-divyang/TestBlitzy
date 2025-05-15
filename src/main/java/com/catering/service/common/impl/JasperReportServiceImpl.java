package com.catering.service.common.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.constant.Constants;
import com.catering.constant.JasperConstantPatternForDecimalPoint;
import com.catering.constant.ReportParameterConstants;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.service.common.FileService;
import com.catering.service.common.JasperReportService;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.ReportCompanyDetailRightsService;
import com.catering.util.DecimalPatternUtils;
import com.catering.util.FileUtils;
import com.catering.util.JasperUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * Implements the {@link JasperReportService} interface to provide functionality
 * for generating, configuring, and exporting JasperReports in various formats such as PDF and DOCX.
 * The class uses services and utilities like fileService, httpServletRequest, cacheManager,
 * and companySettingService to support its operations and customize report outputs.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JasperReportServiceImpl implements JasperReportService {

	Logger logger = LoggerFactory.getLogger(JasperReportServiceImpl.class);

	/**
	 * Service for handling file-related operations.
	 */
	FileService fileService;

	/**
	 * Represents the HTTP request received from the client.
	 */
	HttpServletRequest httpServletRequest;

	/**
	 * Manages caching operations to improve application performance.
	 */
	CacheManager cacheManager;

	/**
	 * Service for managing company-specific settings and configurations.
	 */
	CompanySettingService companySettingService;

	ReportCompanyDetailRightsService reportCompanyDetailRightsService;

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#generatePdfReport(List, Map, String)} for details.
	 */
	@Override
	public <T> FileBean generatePdfReport(List<T> data, Map<String, Object> parameters, String jasperPath) {
		if (Objects.nonNull(parameters)) {
			setSettings(parameters);
		}
		JasperReport jasperReport = getJasperReport(jasperPath);
		JasperPrint jasperPrint = getJasperPrint(data, parameters, jasperReport);
		return exportPdf(jasperPrint);
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#generatePdfReport(Map, String)} for details.
	 */
	@Override
	public FileBean generatePdfReport(Map<String, Object> parameters, String jasperPath) {
		setSettings(parameters);
		JasperReport jasperReport = getJasperReport(jasperPath);
		JasperPrint jasperPrint = getJasperPrint(parameters, jasperReport);
		return exportPdf(jasperPrint);
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#generateDocReport(List, Map, String)} for details.
	 */
	@Override
	public <T> FileBean generateDocReport(List<T> data, Map<String, Object> parameters, String jasperPath) {
		if (Objects.nonNull(parameters)) {
			setSettings(parameters);
		}
		JasperReport jasperReport = getJasperReport(jasperPath);
		JasperPrint jasperPrint = getJasperPrint(data, parameters, jasperReport);
		return exportDoc(jasperPrint);
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setCompanyLogo(Map, HttpServletRequest)} for details.
	 */
	@Override
	public Map<String, Object> setCompanyLogo(Map<String, Object> parameters, String reportName, HttpServletRequest request) {
		String image = getCompanyLogo(request);
		if (image != null) {
			ReportCompanyDetailRightsDto reportCompanyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
			if (reportCompanyDetailRightsDto != null && Boolean.TRUE.equals(reportCompanyDetailRightsDto.getCompanyLogo())) {
				parameters.put(Constants.LOGO.toUpperCase(), image);
			}
		}
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JasperReport getJasperReport(String jasperPath) {
		try {
			Object tenant = httpServletRequest.getAttribute(Constants.TENANT);
			String path = JasperUtils.buildPath(tenant, jasperPath);
			// Load the compiled JasperReport (.jasper file) from the class path
			InputStream jasperInput = new ClassPathResource(path).getInputStream();
			// Create a JasperReport object directly from the compiled .jasper file
			return (JasperReport) JRLoader.loadObject(jasperInput);
		} catch (IOException | JRException e) {
			Object foldername = Constants.GENERAL_REPORT;
			String path = JasperUtils.buildPath(foldername, jasperPath);
			// Load the compiled JasperReport (.jasper file) from the classpath
			try {
				InputStream jasperInput = new ClassPathResource(path).getInputStream();
				return (JasperReport) JRLoader.loadObject(jasperInput);
			} catch (IOException exception) {
				return null;
			} catch (JRException exception) {
				logger.error(exception.getMessage(), exception);
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCompanyLogo(HttpServletRequest request) {
		Object tenant = request.getAttribute(Constants.TENANT);
		Cache cache = cacheManager.getCache(Constants.COMPANY_LOGO_CACHE);
		if (cache != null) {
			String cacheKey = tenant + "-" + Constants.LOGO;
			Object cachedValueObject = cache.get(cacheKey, Object.class);
			if (cachedValueObject == null) {
				// Cache miss: Fetch the logo and put it in the cache
				return FileUtils.getCompanyLogoAndSetInCache(fileService, cache, tenant);
			}
			// Cache hit: return as String
			if (cachedValueObject instanceof String cachedValue) {
				return cachedValue;
			}
		}
		return null; // Return null if cache is not available or logo is not found
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setBackgroundImageInReport(Map)} for details.
	 */
	@Override
	public Map<String, Object> setBackgroundImageInReport(Map<String, Object> parameters) {
		parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.REPORT_BACKGROUND));
		parameters.put(ReportParameterConstants.BACKGROUND_FRAME, fileService.getUrl(Constants.REPORT_FRAME));
		parameters.put(ReportParameterConstants.BACKGROUND_LAST_IMAGE, fileService.getUrl(Constants.BACKGROUND_LAST));
		parameters.put(ReportParameterConstants.TERMS_AND_CONDITIONS_IMAGE, fileService.getUrl(Constants.REPORT_TERMS_AND_CONDITIONS));
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setBackgroundImageInGeneralReport(Map, String, String)} for details.
	 */
	@Override
	public Map<String, Object> setBackgroundImageInGeneralReport(Map<String, Object> parameters, String jasperpath, String reportName) {
		// Build the path for the general folder
		String path = JasperUtils.buildPath(httpServletRequest.getAttribute(Constants.TENANT), jasperpath);
		try {
			// Check if the report exists in the specified path
			new ClassPathResource(path).getInputStream();
		} catch (IOException e) {
			// If exception occurs, proceed as it means the report is in the general folder
			ReportCompanyDetailRightsDto companyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
			String backgroundImage = fileService.getUrl(Constants.GENERAL_REPORT_BACKGROUND);
			if (Objects.nonNull(backgroundImage) && Boolean.TRUE.equals(companyDetailRightsDto.getBackgroundImage())) {
				parameters.put(ReportParameterConstants.GENERAL_REPORT_BACKGROUND_IMAGE, backgroundImage);
			}
		}
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setExclusiveImageInReport(Map)} for details.
	 */
	@Override
	public Map<String, Object> setExclusiveImageInReport(Map<String, Object> parameters) {
		parameters.put(ReportParameterConstants.EXCLUSIVE_DESIGN, fileService.createUrl(Constants.REPORT_EXCLUSIVE_IMAGE));
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setDecimalPatterns(Map)} for details.
	 */
	@Override
	public Map<String, Object> setDecimalPatterns(Map<String, Object> parameters) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		parameters.put(ReportParameterConstants.DECIMAL_LIMIT_FOR_CURRENCY, DecimalPatternUtils.getPecisionPatternForAmountJasper(companySettingDto.getDecimalLimitForCurrency()));
		parameters.put(ReportParameterConstants.PRECISION_FOR_QTY_ZERO_DIGIT, JasperConstantPatternForDecimalPoint.ZERO_DECIMAL);
		parameters.put(ReportParameterConstants.PRECISION_FOR_QTY_ONE_DIGIT, JasperConstantPatternForDecimalPoint.ONE_DECIMAL);
		parameters.put(ReportParameterConstants.PRECISION_FOR_QTY_TWO_DIGIT, JasperConstantPatternForDecimalPoint.TWO_DECIMAL);
		parameters.put(ReportParameterConstants.PRECISION_FOR_QTY_THREE_DIGIT, JasperConstantPatternForDecimalPoint.THREE_DECIMAL);
		parameters.put(ReportParameterConstants.PRECISION_FOR_QTY_FOUR_DIGIT, JasperConstantPatternForDecimalPoint.FOUR_DECIMAL);
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setSloganMenuDesignInReport(Map)} for details.
	 */
	@Override
	public Map<String, Object> setSloganMenuDesignInReport(Map<String, Object> parameters) {
		parameters.put(ReportParameterConstants.MENU_WITH_SLOGAN_DESIGN, fileService.createUrl(Constants.REPORT_SLOGAN_MENU_DESIGN));
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setCustomPackageReportBackImage(Map)} for details.
	 */
	@Override
	public Map<String, Object> setCustomPackageReportBackImage(Map<String, Object> parameters) {
		parameters.put(ReportParameterConstants.REPORT_PACKAGE_BACKGROUND, fileService.createUrl(Constants.REPORT_PACKAGE_BACKGROUND));
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 * See {@link JasperReportService#setFunctionIconInReport(Map)} for details.
	 */
	@Override
	public Map<String, Object> setFunctionIconInReport(Map<String, Object> parameters) {
		parameters.put(ReportParameterConstants.REPORT_SPOON, fileService.getUrl(Constants.REPORT_SPOON));
		parameters.put(ReportParameterConstants.REPORT_RUPPES, fileService.getUrl(Constants.REPORT_RUPPES));
		parameters.put(ReportParameterConstants.REPORT_PERSON, fileService.getUrl(Constants.REPORT_PERSON));
		parameters.put(ReportParameterConstants.REPORT_DATE, fileService.getUrl(Constants.REPORT_DATE));
		parameters.put(ReportParameterConstants.REPORT_BACKGROUND_LINE, fileService.getUrl(Constants.REPORT_BACKGROUND_LINE));
		parameters.put(ReportParameterConstants.REPORT_CALL, fileService.getUrl(Constants.REPORT_CALL));
		parameters.put(ReportParameterConstants.REPORT_WHATSAPP, fileService.getUrl(Constants.REPORT_WHATSAPP));
		parameters.put(ReportParameterConstants.REPORT_ADDRESS, fileService.getUrl(Constants.REPORT_ADDRESS));
		return parameters;
	}

	/**
	 * Configures the report parameters with the company's settings such as date and time format, time zone, etc.
	 *
	 * @param parameters A map containing the parameters for the report. This method updates the map with settings.
	 *					 including date format, time format, date time format, time zone, and other related configurations.
	 */
	private void setSettings(Map<String, Object> parameters) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		String dateFormat = companySettingDto.getDateFormat();
		parameters.put(ReportParameterConstants.DATE_FORMAT, dateFormat);
		parameters.put(ReportParameterConstants.DATE_TIME_FORMAT, Boolean.TRUE.equals(companySettingDto.getIs24Hour()) ? dateFormat+" HH:mm" : dateFormat+" hh:mm a");
		parameters.put(ReportParameterConstants.TIME_FORMAT, Boolean.TRUE.equals(companySettingDto.getIs24Hour()) ? " HH:mm" : " hh:mm a");
		parameters.put(ReportParameterConstants.TIME_FORMAT_WITHOUT_TIMEZONE, "hh:mm");
		parameters.put(ReportParameterConstants.TIME_ZONE, companySettingDto.getTimeZone());
	}

	/**
	 * Exports the given JasperPrint object to a PDF file.
	 *
	 * @param jasperPrint The JasperPrint object representing the report to be exported.
	 * @return A FileBean object containing the exported PDF file and its metadata, or null in case of an exception.
	 */
	private FileBean exportPdf(JasperPrint jasperPrint) {
		try {
			return setUpdatedPdfWithMargin(FileBean.builder().contentType(MediaType.APPLICATION_PDF_VALUE).file(JasperExportManager.exportReportToPdf(jasperPrint)).build());
		} catch (JRException exception) {
			logger.error(exception.getMessage(), exception);
		}
		return null;
	}

	/**
	 * Exports the given JasperPrint object to a DOCX file.
	 *
	 * @param jasperPrint The JasperPrint object representing the report to be exported.
	 * @return A FileBean object containing the exported DOCX file and its metadata.
	 */
	private FileBean exportDoc(JasperPrint jasperPrint) {
		removeBackgroundImagesFromReport(jasperPrint);
		JRDocxExporter exporter = new JRDocxExporter();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		try {
			exporter.exportReport();
		} catch (JRException e) {
			logger.error(e.getMessage(), e);
		}
		return FileBean.builder().contentType(null).file(byteArrayOutputStream.toByteArray()).build();
	}

	/**
	 * Removes all background images from the JasperPrint object to prevent extra blank pages.
	 *
	 * @param jasperPrint The JasperPrint object to modify.
	 */
	private void removeBackgroundImagesFromReport(JasperPrint jasperPrint) {
		List<JRPrintPage> pages = jasperPrint.getPages();
		if (pages != null) {
			for (JRPrintPage page : pages) {
				List<JRPrintElement> elements = page.getElements();
				elements.removeIf(element -> element instanceof JRPrintImage); // Remove all images
			}
		}
	}

	/**
	 * Generates a JasperPrint object by filling a JasperReport with the provided data and parameters.
	 *
	 * @param data The list of data objects to be used as the data source for the report.
	 * @param parameters A map of parameter key-value pairs to be included in the report.
	 * @param jasperReport The pre-compiled JasperReport object to be filled with data.
	 * @return The filled JasperPrint object representing the generated report, or null in case of an error.
	 */
	private <T> JasperPrint getJasperPrint(List<T> data, Map<String, Object> parameters, JasperReport jasperReport) {
		try {
			return JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(data));
		} catch (JRException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Generates a JasperPrint object by populating the specified JasperReport with the provided parameters.
	 *
	 * @param parameters A map containing the parameters to be used in the report. These parameters are
	 *					 passed to the report for customization or data manipulation.
	 * @param jasperReport The compiled JasperReport object to be filled with data.
	 * @return A JasperPrint object representing the filled report, or null if an exception occurs.
	 */
	private JasperPrint getJasperPrint(Map<String, Object> parameters, JasperReport jasperReport) {
		try {
			return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource(1));
		} catch (JRException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Updates the PDF file with an extra left margin if specified in the company settings.
	 * If no extra left margin is set, the original PDF is returned unchanged.
	 *
	 * @param oldPdfData The original FileBean containing the PDF file.
	 * @return The updated FileBean with the modified PDF if margins are applied; 
	 *		   otherwise, returns the original FileBean. Returns {@code null} if an error occurs.
	 */
	private FileBean setUpdatedPdfWithMargin(FileBean oldPdfData) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		// If no extra left margin is set then return same pdf without apply margin
		if (companySettingDto.getExtraLeftMarginReport() == 0) {
			return oldPdfData;
		}
		try {
			PDDocument document = PDDocument.load(oldPdfData.getFile());
			setMarginsBasedOnOrientation(document, companySettingDto.getExtraLeftMarginReport());
			// Save the modified PDF
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			document.save(outputStream);
			document.close();
			// Update the FileBean
			oldPdfData.setFile(outputStream.toByteArray());
			return oldPdfData;
		} catch (IOException exception) {
			logger.error(exception.getMessage(), exception);
			return oldPdfData;
		}
	}

	/**
	 * Adjusts the left margin of each page in the given PDF document based on its orientation.
	 *
	 * @param document The PDF document whose margins need to be adjusted.
	 * @param leftMargin The extra left margin to be applied to each page.
	 */
	private void setMarginsBasedOnOrientation(PDDocument document, float leftMargin) {
		document.getPages().forEach(page -> {
			PDRectangle mediaBox = page.getMediaBox();
			boolean isLandscape = page.getRotation() == 90 || page.getRotation() == 270;
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, true, true)) {
				if (!isLandscape) {
					contentStream.transform(new Matrix((mediaBox.getWidth() - leftMargin) / mediaBox.getWidth(), 0, 0, 1, leftMargin, 0));
				} else {
					contentStream.transform(new Matrix(1, 0, 0, (mediaBox.getHeight() - leftMargin) / mediaBox.getHeight(), 0, leftMargin));
				}
			} catch (IOException exception) {
				logger.error(exception.getMessage(), exception);
			}
		});
	}

}