package com.catering.service.tenant.impl;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.dao.order.BookOrderNativeQueryDao;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.TableMenuReportNotesDto;
import com.catering.model.tenant.TableMenuReportFooterNotesModel;
import com.catering.repository.tenant.TableMenuReportFooterNotesRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.TableMenuReportFooterNotesService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the TableMenuReportFooterNotesService interface, providing
 * operations for managing footer notes of a table menu report.
 * This class includes functionality for retrieving and saving footer notes
 * associated with a specified order.
 *
 * The implementation uses a repository for database operations and a model mapper
 * service for converting between entity and DTO objects. Additionally, it leverages
 * a data access object (DAO) for fetching customer-specific details.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableMenuReportFooterNotesServiceImpl implements TableMenuReportFooterNotesService {

	/**
	 * Repository for managing footer notes in table menu reports.
	 */
	TableMenuReportFooterNotesRepository tableMenuReportFooterNotesRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * DAO for executing native queries related to orders.
	 */
	BookOrderNativeQueryDao bookOrderNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableMenuReportNotesDto getTableMenuReportFooterNotes(Long orderId) {
		TableMenuReportFooterNotesModel tableMenuReportFooterNotesModel = tableMenuReportFooterNotesRepository.findByOrderId(orderId);
		DateWiseReportDropDownCommonDto customerName = bookOrderNativeQueryDao.getCustomerNameForTableMenuFooterNotes(orderId);
		if (Objects.nonNull(tableMenuReportFooterNotesModel)) {
			TableMenuReportNotesDto tableMenuReportHeaderNotesDto =  modelMapperService.convertEntityAndDto(tableMenuReportFooterNotesModel, TableMenuReportNotesDto.class);
			if (StringUtils.isNotBlank(tableMenuReportHeaderNotesDto.getNameDefaultLang())) {
				tableMenuReportHeaderNotesDto.setNameDefaultLang(CKEditorContentUtils.convertToCkEditorForm(tableMenuReportHeaderNotesDto.getNameDefaultLang()));
			}
			if (StringUtils.isNotBlank(tableMenuReportHeaderNotesDto.getNamePreferLang())) {
				tableMenuReportHeaderNotesDto.setNamePreferLang(CKEditorContentUtils.convertToCkEditorForm(tableMenuReportHeaderNotesDto.getNamePreferLang()));
			}
			if (StringUtils.isNotBlank(tableMenuReportHeaderNotesDto.getNameSupportiveLang())) {
				tableMenuReportHeaderNotesDto.setNameSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(tableMenuReportHeaderNotesDto.getNameSupportiveLang()));
			}
			return tableMenuReportHeaderNotesDto;
		} else if (Objects.nonNull(customerName)) {
			TableMenuReportNotesDto tableMenuReportHeaderNotesDto = new TableMenuReportNotesDto();
			if (StringUtils.isNotBlank(customerName.getNameDefaultLang())) {
				tableMenuReportHeaderNotesDto.setNameDefaultLang(CKEditorContentUtils.convertToCkEditorForm(customerName.getNameDefaultLang()));
			}
			if (StringUtils.isNotBlank(customerName.getNamePreferLang())) {
				tableMenuReportHeaderNotesDto.setNamePreferLang(CKEditorContentUtils.convertToCkEditorForm(customerName.getNamePreferLang()));
			}
			if (StringUtils.isNotBlank(customerName.getNameSupportiveLang())) {
				tableMenuReportHeaderNotesDto.setNameSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(customerName.getNameSupportiveLang()));
			}
			return tableMenuReportHeaderNotesDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<TableMenuReportNotesDto> saveTableMenuReportFooterNotes(TableMenuReportNotesDto tableMenuReportFooterNotesDto) {
		if (StringUtils.isNotBlank(tableMenuReportFooterNotesDto.getNameDefaultLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(tableMenuReportFooterNotesDto.getNameDefaultLang()));
			tableMenuReportFooterNotesDto.setNameDefaultLang((document.toString()));
		}
		if (StringUtils.isNotBlank(tableMenuReportFooterNotesDto.getNamePreferLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(tableMenuReportFooterNotesDto.getNamePreferLang()));
			tableMenuReportFooterNotesDto.setNamePreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(tableMenuReportFooterNotesDto.getNameSupportiveLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(tableMenuReportFooterNotesDto.getNameSupportiveLang()));
			tableMenuReportFooterNotesDto.setNameSupportiveLang(document.toString());
		}
		TableMenuReportFooterNotesModel tableMenuReportFooterNotesModel = modelMapperService.convertEntityAndDto(tableMenuReportFooterNotesDto, TableMenuReportFooterNotesModel.class);
		if (tableMenuReportFooterNotesRepository.existsByOrderId(tableMenuReportFooterNotesDto.getOrderId()) && tableMenuReportFooterNotesDto.getId() == null) {
			tableMenuReportFooterNotesRepository.updateRecord(tableMenuReportFooterNotesDto.getOrderId(), tableMenuReportFooterNotesDto.getNameDefaultLang(), tableMenuReportFooterNotesDto.getNamePreferLang(), tableMenuReportFooterNotesDto.getNameSupportiveLang());
			return Optional.of(modelMapperService.convertEntityAndDto(tableMenuReportFooterNotesRepository.findByOrderId(tableMenuReportFooterNotesDto.getOrderId()), TableMenuReportNotesDto.class));
		} else {
			DataUtils.setAuditFields(tableMenuReportFooterNotesRepository, tableMenuReportFooterNotesDto.getId(), tableMenuReportFooterNotesModel);
			return Optional.of(modelMapperService.convertEntityAndDto(tableMenuReportFooterNotesRepository.save(tableMenuReportFooterNotesModel), TableMenuReportNotesDto.class));
		}
	}

}