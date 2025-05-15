package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.CrockeryNotesDto;
import com.catering.model.tenant.CrockeryNotesModel;
import com.catering.repository.tenant.CrockeryNotesRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.CrockeryNotesService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the CrockeryNotesService interface.
 * This service is responsible for managing crockery notes data, including retrieving and saving crockery notes information.
 * It interacts with the repository layer to handle persistence and utilizes a model mapper for entity-to-DTO conversions.
 * 
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CrockeryNotesServiceImpl implements CrockeryNotesService {

	/**
	 * Repository for managing Crockery Notes entities.
	 */
	CrockeryNotesRepository crockeryNotesRepository;

	/**
	 * Service for mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CrockeryNotesDto getCrockeryNotes() {
		List<CrockeryNotesModel> crockeryNotesModel = crockeryNotesRepository.findAll();
		if (Objects.nonNull(crockeryNotesModel) && !crockeryNotesModel.isEmpty()) {
			CrockeryNotesDto crockeryNotesDto =  modelMapperService.convertEntityAndDto(crockeryNotesModel.get(0), CrockeryNotesDto.class);
			if (StringUtils.isNotBlank(crockeryNotesDto.getNameDefaultLang())) {
				crockeryNotesDto.setNameDefaultLang(CKEditorContentUtils.convertToCkEditorForm(crockeryNotesDto.getNameDefaultLang()));
			}
			if (StringUtils.isNotBlank(crockeryNotesDto.getNamePreferLang())) {
				crockeryNotesDto.setNamePreferLang(CKEditorContentUtils.convertToCkEditorForm(crockeryNotesDto.getNamePreferLang()));
			}
			if (StringUtils.isNotBlank(crockeryNotesDto.getNameSupportiveLang())) {
				crockeryNotesDto.setNameSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(crockeryNotesDto.getNameSupportiveLang()));
			}
			return crockeryNotesDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CrockeryNotesDto> saveCrockeryNotes(CrockeryNotesDto crockeryNotesDto) {
		if (StringUtils.isNotBlank(crockeryNotesDto.getNameDefaultLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(crockeryNotesDto.getNameDefaultLang()));
			crockeryNotesDto.setNameDefaultLang((document.toString()));
		}
		if (StringUtils.isNotBlank(crockeryNotesDto.getNamePreferLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(crockeryNotesDto.getNamePreferLang()));
			crockeryNotesDto.setNamePreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(crockeryNotesDto.getNameSupportiveLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(crockeryNotesDto.getNameSupportiveLang()));
			crockeryNotesDto.setNameSupportiveLang(document.toString());
		}
		CrockeryNotesModel crockeryNotesModel = modelMapperService.convertEntityAndDto(crockeryNotesDto, CrockeryNotesModel.class);
		List<CrockeryNotesModel> crockeryNotesModelList = crockeryNotesRepository.findAll();
		if (Objects.nonNull(crockeryNotesModelList) && !crockeryNotesModelList.isEmpty()) {
			crockeryNotesModel.setId(crockeryNotesModelList.get(0).getId());
		}
		DataUtils.setAuditFields(crockeryNotesRepository, crockeryNotesDto.getId(), crockeryNotesModel);
		return Optional.of(modelMapperService.convertEntityAndDto(crockeryNotesRepository.save(crockeryNotesModel), CrockeryNotesDto.class));
	}

}