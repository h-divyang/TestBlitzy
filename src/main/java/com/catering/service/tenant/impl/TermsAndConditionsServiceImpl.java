package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.TermsAndConditionsDto;
import com.catering.model.tenant.TermsAndConditionsModel;
import com.catering.repository.tenant.TermsAndConditionsRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.TermsAndConditionsService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import com.catering.util.PdfUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the TermsAndConditionsService interface providing methods to manage terms and conditions data.
 *
 * @param <TermsAndConditionsDto>	Data Transfer Object (DTO) representing terms and conditions.
 * @param <TermsAndConditionsModel> Entity model class representing terms and conditions.
 * @param <Long>					Type of the entity's primary key.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @since 2023-12-18
 * @author Krushali Talaviya
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TermsAndConditionsServiceImpl extends GenericServiceImpl<TermsAndConditionsDto, TermsAndConditionsModel, Long> implements TermsAndConditionsService {

	/**
	 * Repository for managing terms and conditions data.
	 */
	 TermsAndConditionsRepository termsAndConditionsRepository;

	 /**
	  * Service for mapping models and DTOs.
	  */
	 ModelMapperService modelMapperService;

	 /**
	  * Utility for loading resources in the application context.
	  */
	 ResourceLoader resourceLoader;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermsAndConditionsDto getTermsAndConditionsData() {
		List<TermsAndConditionsModel> termsAndConditionsModel = termsAndConditionsRepository.findAll();
		if (Objects.nonNull(termsAndConditionsModel)  && !termsAndConditionsModel.isEmpty()) {
			TermsAndConditionsDto termsAndConditionsDto =  modelMapperService.convertEntityAndDto(termsAndConditionsModel.get(0), TermsAndConditionsDto.class);
			if (StringUtils.isNotBlank(termsAndConditionsDto.getContentDefaultLang())) {
				termsAndConditionsDto.setContentDefaultLang(CKEditorContentUtils.convertToCkEditorForm(termsAndConditionsDto.getContentDefaultLang()));
			}
			if (StringUtils.isNotBlank(termsAndConditionsDto.getContentPreferLang())) {
				termsAndConditionsDto.setContentPreferLang(CKEditorContentUtils.convertToCkEditorForm(termsAndConditionsDto.getContentPreferLang()));
			}
			if (StringUtils.isNotBlank(termsAndConditionsDto.getContentSupportiveLang())) {
				termsAndConditionsDto.setContentSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(termsAndConditionsDto.getContentSupportiveLang()));
			}
			return termsAndConditionsDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<TermsAndConditionsDto> saveTermsAndConitionsData(TermsAndConditionsDto termsAndConditionsDto) {
		if (termsAndConditionsDto.getContentDefaultLang() == null) {
			termsAndConditionsDto.setContentDefaultLang("");
		} else if (!"".equals(termsAndConditionsDto.getContentDefaultLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(termsAndConditionsDto.getContentDefaultLang()));
			termsAndConditionsDto.setContentDefaultLang(document.toString());
		}
		if (termsAndConditionsDto.getContentPreferLang() == null) {
			termsAndConditionsDto.setContentPreferLang("");
		} else if (!"".equals(termsAndConditionsDto.getContentPreferLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(termsAndConditionsDto.getContentPreferLang()));
			termsAndConditionsDto.setContentPreferLang(document.toString());
		}
		if (termsAndConditionsDto.getContentSupportiveLang() == null) {
			termsAndConditionsDto.setContentSupportiveLang("");
		} else if (!"".equals(termsAndConditionsDto.getContentSupportiveLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(termsAndConditionsDto.getContentSupportiveLang()));
			termsAndConditionsDto.setContentSupportiveLang(document.toString());
		}
		TermsAndConditionsModel termsAndConditionsModel = modelMapperService.convertEntityAndDto(termsAndConditionsDto, TermsAndConditionsModel.class);
		List<TermsAndConditionsModel> termsAndConditionsModelList = termsAndConditionsRepository.findAll();
		if (Objects.nonNull(termsAndConditionsModelList) && !termsAndConditionsModelList.isEmpty()) {
			termsAndConditionsModel.setId(termsAndConditionsModelList.get(0).getId());
		}
		DataUtils.setAuditFields(termsAndConditionsRepository, termsAndConditionsDto.getId(), termsAndConditionsModel);
		return Optional.of(modelMapperService.convertEntityAndDto(termsAndConditionsRepository.save(termsAndConditionsModel), TermsAndConditionsDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getTermsAndConditionsInByte(Integer langType) {
		String htmlData = termsAndConditionsRepository.getTermsAndConditionsReportData(langType);
		return PdfUtils.convertHtmlToPdf(htmlData, resourceLoader);
	}

}