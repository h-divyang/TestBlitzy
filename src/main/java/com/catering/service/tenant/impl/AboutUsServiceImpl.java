package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.AboutUsDto;
import com.catering.model.tenant.AboutUsModel;
import com.catering.repository.tenant.AboutUsRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.AboutUsService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the AboutUsService interface for managing "About Us" data.
 * This service provides methods to retrieve and save "About Us" information
 * in multiple languages and prepares data for CKEditor compatibility as needed.
 * Uses AboutUsRepository for data persistence and ModelMapperService for entity-to-DTO conversion.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AboutUsServiceImpl implements AboutUsService {

	/**
	 * Repository interface for accessing and managing About Us entity data in the database.
	 */
	AboutUsRepository aboutUsRepository;

	/**
	 * Service encapsulating the logic for mapping objects with ModelMapper.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AboutUsDto getAboutUsData() {
		List<AboutUsModel> aboutUsModel = aboutUsRepository.findAll();
		if (Objects.nonNull(aboutUsModel) && !aboutUsModel.isEmpty()) {
			AboutUsDto aboutUsDto =  modelMapperService.convertEntityAndDto(aboutUsModel.get(0), AboutUsDto.class);
			if (StringUtils.isNotBlank(aboutUsDto.getAboutUsDefaultLang())) {
				aboutUsDto.setAboutUsDefaultLang(CKEditorContentUtils.convertToCkEditorForm(aboutUsDto.getAboutUsDefaultLang()));
			}
			if (StringUtils.isNotBlank(aboutUsDto.getAboutUsPreferLang())) {
				aboutUsDto.setAboutUsPreferLang(CKEditorContentUtils.convertToCkEditorForm(aboutUsDto.getAboutUsPreferLang()));
			}
			if (StringUtils.isNotBlank(aboutUsDto.getAboutUsSupportiveLang())) {
				aboutUsDto.setAboutUsSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(aboutUsDto.getAboutUsSupportiveLang()));
			}
			return aboutUsDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<AboutUsDto> saveAboutUsData(AboutUsDto aboutUsDto) {
		if (StringUtils.isNotBlank(aboutUsDto.getAboutUsDefaultLang())) {
			Document document = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(aboutUsDto.getAboutUsDefaultLang()));
			aboutUsDto.setAboutUsDefaultLang(document.toString());
		}
		if (StringUtils.isNotBlank(aboutUsDto.getAboutUsPreferLang())) {
			Document document = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(aboutUsDto.getAboutUsPreferLang()));
			aboutUsDto.setAboutUsPreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(aboutUsDto.getAboutUsSupportiveLang())) {
			Document document = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(aboutUsDto.getAboutUsSupportiveLang()));
			aboutUsDto.setAboutUsSupportiveLang(document.toString());
		}
		AboutUsModel aboutUsModel = modelMapperService.convertEntityAndDto(aboutUsDto, AboutUsModel.class);
		List<AboutUsModel> aboutUsModelList = aboutUsRepository.findAll();
		if (Objects.nonNull(aboutUsModelList) && !aboutUsModelList.isEmpty()) {
			aboutUsModel.setId(aboutUsModelList.get(0).getId());
		}
		DataUtils.setAuditFields(aboutUsRepository, aboutUsDto.getId(), aboutUsModel);
		return Optional.of(modelMapperService.convertEntityAndDto(aboutUsRepository.save(aboutUsModel), AboutUsDto.class));
	}

}