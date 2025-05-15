package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.OrderBookingTemplateNotesDto;
import com.catering.model.tenant.OrderBookingTemplateNotesModel;
import com.catering.repository.tenant.OrderBookingTemplateNotesRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.OrderBookingReportTemplateNotesService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the OrderBookingReportTemplateNotesService interface for managing
 * operations related to order booking report template notes. This class provides methods
 * for retrieving and saving notes in different languages (default, preferred, and supportive)
 * associated with order booking templates.
 *
 * This class interacts with the database through the OrderBookingTemplateNotesRepository
 * to persist and retrieve data and uses the ModelMapperService for object mapping between
 * entities and DTOs. Additional utility methods are used for content processing related to
 * CKEditor-formatted HTML.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderBookingReportTemplateNotesServiceImpl  implements OrderBookingReportTemplateNotesService {

	/**
	 * Repository for managing notes related to order booking templates.
	 */
	OrderBookingTemplateNotesRepository orderBookingTemplateNotesRepository;

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBookingTemplateNotesDto getOrderBookingTemplateNotes() {
		List<OrderBookingTemplateNotesModel> orderBookingReportTemplateNotesModel = orderBookingTemplateNotesRepository.findAll();
		if (Objects.nonNull(orderBookingReportTemplateNotesModel) && !orderBookingReportTemplateNotesModel.isEmpty()) {
			OrderBookingTemplateNotesDto orderBookingTemplateNotesDto =  modelMapperService.convertEntityAndDto(orderBookingReportTemplateNotesModel.get(0), OrderBookingTemplateNotesDto.class);
			if (StringUtils.isNotBlank(orderBookingTemplateNotesDto.getNotesDefaultLang())) {
				orderBookingTemplateNotesDto.setNotesDefaultLang(CKEditorContentUtils.convertToCkEditorForm(orderBookingTemplateNotesDto.getNotesDefaultLang()));
			}
			if (StringUtils.isNotBlank(orderBookingTemplateNotesDto.getNotesPreferLang())) {
				orderBookingTemplateNotesDto.setNotesPreferLang(CKEditorContentUtils.convertToCkEditorForm(orderBookingTemplateNotesDto.getNotesPreferLang()));
			}
			if (StringUtils.isNotBlank(orderBookingTemplateNotesDto.getNotesSupportiveLang())) {
				orderBookingTemplateNotesDto.setNotesSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(orderBookingTemplateNotesDto.getNotesSupportiveLang()));
			}
			return orderBookingTemplateNotesDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<OrderBookingTemplateNotesDto> saveOrderBookingTemplateNotes(OrderBookingTemplateNotesDto orderBookingTemplateNotesDto) {
		if (StringUtils.isNotBlank(orderBookingTemplateNotesDto.getNotesDefaultLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(orderBookingTemplateNotesDto.getNotesDefaultLang()));
			orderBookingTemplateNotesDto.setNotesDefaultLang((document.toString()));
		}
		if (StringUtils.isNotBlank(orderBookingTemplateNotesDto.getNotesPreferLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(orderBookingTemplateNotesDto.getNotesPreferLang()));
			orderBookingTemplateNotesDto.setNotesPreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(orderBookingTemplateNotesDto.getNotesSupportiveLang())) {
			Document document  = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(orderBookingTemplateNotesDto.getNotesSupportiveLang()));
			orderBookingTemplateNotesDto.setNotesSupportiveLang(document.toString());
		}
		OrderBookingTemplateNotesModel orderBookingTemplateNotesModel = modelMapperService.convertEntityAndDto(orderBookingTemplateNotesDto, OrderBookingTemplateNotesModel.class);
		List<OrderBookingTemplateNotesModel> orderBookingTemplateNotesModelList = orderBookingTemplateNotesRepository.findAll();
		if (!orderBookingTemplateNotesModelList.isEmpty()) {
			orderBookingTemplateNotesModel.setId(orderBookingTemplateNotesModelList.get(0).getId());
		}
		DataUtils.setAuditFields(orderBookingTemplateNotesRepository, orderBookingTemplateNotesDto.getId(), orderBookingTemplateNotesModel);
		return Optional.of(modelMapperService.convertEntityAndDto(orderBookingTemplateNotesRepository.save(orderBookingTemplateNotesModel), OrderBookingTemplateNotesDto.class));
	}

}