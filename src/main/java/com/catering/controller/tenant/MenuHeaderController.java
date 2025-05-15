package com.catering.controller.tenant;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.tenant.request.MenuWithUserRightsDto;
import com.catering.service.tenant.MenuHeaderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class that handles menu header related API endpoints.
 * This class is responsible for routing HTTP requests related to menu headers.
 *
 * The class is annotated with @RestController to indicate that it is a controller
 * component that provides RESTful API endpoints.
 * The @RequestMapping annotation specifies the base URL path for the menu header endpoints.
 * The @Tag annotation from Swagger is used to provide a tag name for the API documentation.
 *
 * The controller relies on the MenuHeaderService to handle the business logic
 * related to menu headers.
 *
 * @see RestController
 * @see RequestMapping
 * @since July 2023
 * @author Krushali Talaviya
 *
 */
@RestController
@RequestMapping(value = ApiPathConstant.MENU_HEADER)
@Tag(name = SwaggerConstant.MENU_HEADER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuHeaderController {

	/**
	 * Service for handling business logic related to Menu Headers.
	 */
	MenuHeaderService menuHeaderService;

	/**
	 * Retrieves the menu headers with their associated main menus.
	 * This endpoint is mapped to the HTTP GET method and corresponds to the base URL path.
	 * It returns a list of MainMenuDto objects representing the menu structure.
	 *
	 * @return A list of MainMenuDto objects representing the menu structure.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@Cacheable(value = Constants.SIDEBAR_CACHE, key="#id+'-'+#uniqueCode")
	public List<MenuWithUserRightsDto> read(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id, @RequestParam String uniqueCode) {
		return menuHeaderService.read(Long.parseLong(id));
	}

}