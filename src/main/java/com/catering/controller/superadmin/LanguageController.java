package com.catering.controller.superadmin;

import java.util.List;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.superadmin.LanguageDto;
import com.catering.exception.RestException;
import com.catering.model.superadmin.LanguageModel;
import com.catering.service.common.MessageService;
import com.catering.service.superadmin.LanguageService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The LanguageController class is a REST controller responsible for handling API requests for language-related operations, such as creating and retrieving language data.
 * This controller interacts with the LanguageService and utilizes the MessageService for generating localized feedback messages in API responses.
 */
@RestController
@RequestMapping(value = ApiPathConstant.LANGUAGE)
@Tag(name = SwaggerConstant.LANGUAGE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LanguageController {

	/**
	 * MessageService is an autowired dependency used for generating localized messages based on message keys or keys with additional arguments.
	 * It provides methods to retrieve localized feedback or error messages to be included in API responses within the LanguageController.
	 */
	MessageService messageService;

	/**
	 * LanguageService is an injected dependency responsible for handling business logic related to language-related operations.
	 * It provides methods for creating, updating, and retrieving language data and definitions.
	 */
	LanguageService languageService;

	/**
	 * Creates a new language entry or updates an existing one based on the input data provided in {@code languageDto}.
	 * The method interacts with {@code LanguageService} to handle business logic and {@code RequestResponseUtils} to generate a standardized response.
	 *
	 * @param languageDto the {@code LanguageDto} object containing language details to create or update.
	 * @return a {@code ResponseContainerDto<LanguageDto>} containing the created or updated language data and a success message.
	 * @throws RestException if an error occurs during the creation or update process.
	 */
	@PostMapping
	public ResponseContainerDto<LanguageDto> create(@Valid @RequestBody LanguageDto languageDto) throws RestException {
		LanguageDto languageResponseDto = languageService.createAndUpdate(languageDto, LanguageDto.class, LanguageModel.class, languageDto.getId());
		return RequestResponseUtils.generateResponseDto(languageResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Handles the GET request to retrieve a list of languages based on filters.
	 *
	 * @param filterDto the filtering criteria encapsulated in a {@code FilterDto} object, including pagination and sorting details.
	 * @return a {@code ResponseContainerDto} that contains a list of {@code LanguageDto} objects, representing the requested languages along with metadata such as response status and message.
	 */
	@GetMapping
	public ResponseContainerDto<List<LanguageDto>> read(FilterDto filterDto) {
		return languageService.read(filterDto);
	}

}