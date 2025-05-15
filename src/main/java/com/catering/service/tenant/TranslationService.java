package com.catering.service.tenant;

import com.catering.dto.tenant.request.TranslateDto;

/**
 * Service interface for handling translation operations.
 *
 * This interface defines the method for translating text from a source language
 * into multiple target languages. Implementations of this interface should provide
 * the underlying logic for translation functionality, including interaction with
 * external translation APIs or engines.
 */
public interface TranslationService {

	/**
	 * Translates the provided text from a source language to multiple target languages as specified.
	 *
	 * @param translateDto The data transfer object containing the source language, target languages, and text to be translated.
	 * @return An array of objects containing the translations for each target language. Each object corresponds to a specific translation.
	 */
	Object[] translateText(TranslateDto translateDto);

}