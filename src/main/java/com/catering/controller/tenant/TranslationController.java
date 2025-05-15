package com.catering.controller.tenant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.tenant.request.TranslateDto;
import com.catering.service.tenant.TranslationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(value = ApiPathConstant.AUTO_TRANSLATE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = SwaggerConstant.AUTO_TRANSLATE)
public class TranslationController {

	/**
	 * Service for managing translations and localization.
	 */
	TranslationService translationService;

	/**
	 * Get Data by user Language.
	 * @param translateDto
	 * @return
	 */
	@PostMapping
	public Object[] translateText(@RequestBody TranslateDto translateDto) {
		return translationService.translateText(translateDto);
	}

}