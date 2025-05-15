package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.catering.constant.Constants;
import com.catering.dto.tenant.request.FlagDto;
import com.catering.dto.tenant.request.NotesDto;
import com.catering.model.tenant.NotesModel;
import com.catering.model.tenant.OrderBookingTemplateNotesModel;
import com.catering.repository.tenant.NotesRepository;
import com.catering.repository.tenant.OrderBookingTemplateNotesRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.NotesService;
import com.catering.util.CKEditorContentUtils;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link NotesService} interface.
 * Provides business logic to handle operations related to notes such as saving notes,
 * retrieving notes by order ID, and determining visibility flags for notes and reports.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotesServiceImpl implements NotesService {

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing notes.
	 */
	NotesRepository notesRepository;

	/**
	 * Repository for managing notes related to order booking templates.
	 */
	OrderBookingTemplateNotesRepository orderBookingTemplateNotesRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<NotesDto> saveNotes(@Valid NotesDto notesDto) {
		if (StringUtils.isNotBlank(notesDto.getNotesDefaultLang())) {
			Document document = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(notesDto.getNotesDefaultLang()));
			notesDto.setNotesDefaultLang(document.toString());
		}
		if (StringUtils.isNotBlank(notesDto.getNotesPreferLang())) {
			Document document = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(notesDto.getNotesPreferLang()));
			notesDto.setNotesPreferLang(document.toString());
		}
		if (StringUtils.isNotBlank(notesDto.getNotesSupportiveLang())) {
			Document document = Jsoup.parse(CKEditorContentUtils.modifyHtmlContent(notesDto.getNotesSupportiveLang()));
			notesDto.setNotesSupportiveLang(document.toString());
		}
		NotesModel notesModel = modelMapperService.convertEntityAndDto(notesDto, NotesModel.class);
		DataUtils.setAuditFields(notesRepository, notesDto.getId(), notesModel);
		notesModel.setId(notesDto.getId());
		return Optional.of(modelMapperService.convertEntityAndDto(notesRepository.save(notesModel), NotesDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotesDto getNotes(Long orderId) {
		NotesModel notesModel = notesRepository.findByBookOrderId(orderId).orElse(null);
		List<OrderBookingTemplateNotesModel> notesModelLatestRecord = orderBookingTemplateNotesRepository.findAll();
		if (Objects.nonNull(notesModel)) {
			NotesDto notesDto = modelMapperService.convertEntityAndDto(notesModel, NotesDto.class);
			if(StringUtils.isNoneBlank(notesDto.getNotesDefaultLang())) {
				notesDto.setNotesDefaultLang(CKEditorContentUtils.convertToCkEditorForm(notesDto.getNotesDefaultLang()));
			}
			if(StringUtils.isNoneBlank(notesDto.getNotesPreferLang())) {
				notesDto.setNotesPreferLang(CKEditorContentUtils.convertToCkEditorForm(notesDto.getNotesPreferLang()));
			}
			if(StringUtils.isNoneBlank(notesDto.getNotesSupportiveLang())) {
				notesDto.setNotesSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(notesDto.getNotesSupportiveLang()));
			}
			return notesDto;
		} else if(Objects.nonNull(notesModelLatestRecord) && !notesModelLatestRecord.isEmpty()) {
			NotesDto notesDto = modelMapperService.convertEntityAndDto(notesModelLatestRecord.get(0), NotesDto.class);
			if (StringUtils.isNoneBlank(notesDto.getNotesDefaultLang())) {
				notesDto.setNotesDefaultLang(CKEditorContentUtils.convertToCkEditorForm(notesDto.getNotesDefaultLang()));
			}
			if (StringUtils.isNoneBlank(notesDto.getNotesPreferLang())) {
				notesDto.setNotesPreferLang(CKEditorContentUtils.convertToCkEditorForm(notesDto.getNotesPreferLang()));
			}
			if (StringUtils.isNoneBlank(notesDto.getNotesSupportiveLang())) {
				notesDto.setNotesSupportiveLang(CKEditorContentUtils.convertToCkEditorForm(notesDto.getNotesSupportiveLang()));
			}
			notesDto.setId(null);
			return notesDto;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FlagDto getFlagForNotesAndReports(HttpServletRequest request) {
		Object tenant = request.getAttribute(Constants.TENANT);
		return new FlagDto(
				Constants.SHYAM_CATERERS_SURAT_TENANT.equals(tenant) || Constants.SHYAM_CATERERS_AMRELI_TENANT.equals(tenant) || Constants.JASRAJ_UTTAM_CATERERS.equals(tenant),
				Constants.BRIJ_BHOG_CATERERS.equals(tenant) || Constants.SHYAM_CATERERS_SURAT_TENANT.equals(tenant) || Constants.SHYAM_CATERERS_AMRELI_TENANT.equals(tenant));
	}

}