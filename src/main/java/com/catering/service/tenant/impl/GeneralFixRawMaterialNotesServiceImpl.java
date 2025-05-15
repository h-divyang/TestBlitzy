package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.GeneralFixRawMaterialNotesDto;
import com.catering.model.tenant.GeneralFixRawMaterialNotesModel;
import com.catering.repository.tenant.GeneralFixRawMaterialNotesRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.GeneralFixRawMaterialNotesService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the GeneralFixRawMaterialNotesService interface.
 *
 * Provides functionalities for retrieving and saving general fix raw material notes.
 * This class makes use of the GeneralFixRawMaterialNotesRepository for database
 * operations and ModelMapperService for object mapping.
 *
 * All operations involving text content manipulation are processed for CKEditor
 * compatibility, ensuring proper handling of HTML content in the respective language fields.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeneralFixRawMaterialNotesServiceImpl implements GeneralFixRawMaterialNotesService {

	/**
	 * Repository for managing General Fix Raw Material Notes entities.
	 */
	GeneralFixRawMaterialNotesRepository generalFixRawMaterialNotesRepository;

	/**
	 * Service for mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeneralFixRawMaterialNotesDto getGeneralFixRawMaterialNotes() {
		List<GeneralFixRawMaterialNotesModel> generalFixRawMaterialNotesModel = generalFixRawMaterialNotesRepository.findAll();
		if (Objects.nonNull(generalFixRawMaterialNotesModel) && !generalFixRawMaterialNotesModel.isEmpty()) {
			GeneralFixRawMaterialNotesDto generalFixRawMaterialNotesDto =  modelMapperService.convertEntityAndDto(generalFixRawMaterialNotesModel.get(0), GeneralFixRawMaterialNotesDto.class);
			if (StringUtils.isNotBlank(generalFixRawMaterialNotesDto.getNameDefaultLang())) {
				generalFixRawMaterialNotesDto.setNameDefaultLang(CKEditorContentUtils.convertToCkEditorForm(generalFixRawMaterialNotesDto.getNameDefaultLang()));
			}
			if (StringUtils.isNotBlank(generalFixRawMaterialNotesDto.getNamePreferLang())) {
				generalFixRawMaterialNotesDto.setNamePreferLang(CKEditorContentUtils.convertToCkEditorForm(generalFixRawMaterialNotesDto.getNamePreferLang()));
			}
			if (StringUtils.isNotBlank(generalFixRawMaterialNotesDto.getNameSupportiveLang())) {
				generalFixRawMaterialNotesDto.setNameSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(generalFixRawMaterialNotesDto.getNameSupportiveLang()));
			}
			return generalFixRawMaterialNotesDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<GeneralFixRawMaterialNotesDto> saveGeneralFixRawMaterialNotes(GeneralFixRawMaterialNotesDto generalFixRawMaterialNotesDto) {
		if (StringUtils.isNotBlank(generalFixRawMaterialNotesDto.getNameDefaultLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(generalFixRawMaterialNotesDto.getNameDefaultLang()));
			generalFixRawMaterialNotesDto.setNameDefaultLang((document.toString()));
		}
		if (StringUtils.isNotBlank(generalFixRawMaterialNotesDto.getNamePreferLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(generalFixRawMaterialNotesDto.getNamePreferLang()));
			generalFixRawMaterialNotesDto.setNamePreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(generalFixRawMaterialNotesDto.getNameSupportiveLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(generalFixRawMaterialNotesDto.getNameSupportiveLang()));
			generalFixRawMaterialNotesDto.setNameSupportiveLang(document.toString());
		}
		GeneralFixRawMaterialNotesModel generalFixRawMaterialNotesModel = modelMapperService.convertEntityAndDto(generalFixRawMaterialNotesDto, GeneralFixRawMaterialNotesModel.class);
		List<GeneralFixRawMaterialNotesModel> generalFixRawMaterialNotesModelList = generalFixRawMaterialNotesRepository.findAll();
		if (Objects.nonNull(generalFixRawMaterialNotesModelList) && !generalFixRawMaterialNotesModelList.isEmpty()) {
			generalFixRawMaterialNotesModel.setId(generalFixRawMaterialNotesModelList.get(0).getId());
		}
		DataUtils.setAuditFields(generalFixRawMaterialNotesRepository, generalFixRawMaterialNotesDto.getId(), generalFixRawMaterialNotesModel);
		return Optional.of(modelMapperService.convertEntityAndDto(generalFixRawMaterialNotesRepository.save(generalFixRawMaterialNotesModel), GeneralFixRawMaterialNotesDto.class));
	}

}