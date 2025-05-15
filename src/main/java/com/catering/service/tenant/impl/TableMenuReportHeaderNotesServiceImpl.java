package com.catering.service.tenant.impl;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.TableMenuReportNotesDto;
import com.catering.model.tenant.TableMenuReportHeaderNotesModel;
import com.catering.repository.tenant.TableMenuReportHeaderNotesRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.TableMenuReportHeaderNotesService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the TableMenuReportHeaderNotesService interface.
 * Responsible for managing header notes for table menu reports, including retrieving
 * and saving notes associated with specific orders.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableMenuReportHeaderNotesServiceImpl implements TableMenuReportHeaderNotesService {

	/**
	 * Repository for managing header notes in table menu reports.
	 */
	TableMenuReportHeaderNotesRepository tableMenuReportHeaderNotesRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableMenuReportNotesDto getTableMenuReportHeaderNotes(Long orderId) {
		TableMenuReportHeaderNotesModel tableMenuReportHeaderNotesModel = tableMenuReportHeaderNotesRepository.findByOrderId(orderId);
		String groomAndBrideName = tableMenuReportHeaderNotesRepository.getGroomAndBrideName(orderId);
		if (Objects.nonNull(tableMenuReportHeaderNotesModel)) {
			TableMenuReportNotesDto tableMenuReportHeaderNotesDto =  modelMapperService.convertEntityAndDto(tableMenuReportHeaderNotesModel, TableMenuReportNotesDto.class);
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
		} else if (groomAndBrideName != null) {
			TableMenuReportNotesDto tableMenuReportHeaderNotesDto = new TableMenuReportNotesDto();
			tableMenuReportHeaderNotesDto.setNameDefaultLang(CKEditorContentUtils.convertToCkEditorForm(groomAndBrideName));
			return tableMenuReportHeaderNotesDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<TableMenuReportNotesDto> saveTableMenuReportHeaderNotes(TableMenuReportNotesDto tableMenuReportHeaderNotesDto) {
		if (StringUtils.isNotBlank(tableMenuReportHeaderNotesDto.getNameDefaultLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(tableMenuReportHeaderNotesDto.getNameDefaultLang()));
			tableMenuReportHeaderNotesDto.setNameDefaultLang((document.toString()));
		}
		if (StringUtils.isNotBlank(tableMenuReportHeaderNotesDto.getNamePreferLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(tableMenuReportHeaderNotesDto.getNamePreferLang()));
			tableMenuReportHeaderNotesDto.setNamePreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(tableMenuReportHeaderNotesDto.getNameSupportiveLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(tableMenuReportHeaderNotesDto.getNameSupportiveLang()));
			tableMenuReportHeaderNotesDto.setNameSupportiveLang(document.toString());
		}
		TableMenuReportHeaderNotesModel tableMenuReportHeaderNotesModel = modelMapperService.convertEntityAndDto(tableMenuReportHeaderNotesDto, TableMenuReportHeaderNotesModel.class);
		if (tableMenuReportHeaderNotesRepository.existsByOrderId(tableMenuReportHeaderNotesDto.getOrderId()) && tableMenuReportHeaderNotesDto.getId() == null) {
			tableMenuReportHeaderNotesRepository.updateRecord(tableMenuReportHeaderNotesDto.getOrderId(), tableMenuReportHeaderNotesDto.getNameDefaultLang(), tableMenuReportHeaderNotesDto.getNamePreferLang(), tableMenuReportHeaderNotesDto.getNameSupportiveLang());
			return Optional.of(modelMapperService.convertEntityAndDto(tableMenuReportHeaderNotesRepository.findByOrderId(tableMenuReportHeaderNotesDto.getOrderId()), TableMenuReportNotesDto.class));
		} else {
			DataUtils.setAuditFields(tableMenuReportHeaderNotesRepository, tableMenuReportHeaderNotesDto.getId(), tableMenuReportHeaderNotesModel);
			return Optional.of(modelMapperService.convertEntityAndDto(tableMenuReportHeaderNotesRepository.save(tableMenuReportHeaderNotesModel), TableMenuReportNotesDto.class));
		}
	}

}