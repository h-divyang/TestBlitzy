package com.catering.service.superadmin;

import java.util.List;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.superadmin.LanguageDto;
import com.catering.model.superadmin.LanguageModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing and retrieving language-related data.
 *
 * Extends the {@code GenericService} interface to provide common CRUD operations
 * and additional query functionality specific to Language data.
 */
public interface LanguageService extends GenericService<LanguageDto, LanguageModel, Long> {

	/**
	 * Retrieves a list of languages based on the provided filtering criteria.
	 *
	 * @param filterDto The filtering criteria encapsulated in a {@code FilterDto} object, including pagination, sorting, and query details.
	 * @return A {@code ResponseContainerDto} containing a list of {@code LanguageDto} objects that match the filtering criteria,
	 * along with metadata such as response status, message, and paging information.
	 */
	ResponseContainerDto<List<LanguageDto>> read(FilterDto filterDto);

}