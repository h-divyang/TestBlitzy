package com.catering.service.superadmin.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.superadmin.LanguageDto;
import com.catering.model.superadmin.LanguageModel;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.superadmin.LanguageService;

/**
 * Implementation of the {@link LanguageService} interface for managing language-related operations.
 *
 * Extends the {@code GenericServiceImpl} class to reuse common CRUD functionality and override specific methods for
 * handling operations related to {@code LanguageDto} and {@code LanguageModel}.
 * This service primarily focuses on retrieving and manipulating language data, utilizing filtering mechanisms
 * and mapping between DTOs and entity models.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 */
@Service
public class LanguageServiceImpl extends GenericServiceImpl<LanguageDto, LanguageModel, Long> implements LanguageService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<LanguageDto>> read(FilterDto filterDto) {
		return read(LanguageDto.class, LanguageModel.class, filterDto, Optional.empty());
	}

}