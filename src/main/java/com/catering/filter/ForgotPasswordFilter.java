package com.catering.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.catering.config.TenantContext;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.CompanyDto;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanyService;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * ForgotPasswordFilter is a custom filter that extends {@link OncePerRequestFilter} and is responsible for:
 * <ul>
 * <li>Handling password reset requests by validating the provided token and unique code.</li>
 * <li>Setting the tenant and company ID attributes in the request for further processing.</li>
 * <li>Sending appropriate error responses if validation fails.</li>
 * </ul>
 * This filter intercepts password reset requests and checks for valid tokens and company unique codes.
 * If valid, it sets the necessary context attributes (tenant and company ID) for the request.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordFilter extends OncePerRequestFilter {

	/**
	 * Service responsible for handling application-specific messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling company-related data.
	 */
	CompanyService companyService;

	/**
	 * Service for handling JWT-related operations, such as extracting unique codes from tokens.
	 */
	JwtService jwtUtil;

	/**
	 * Processes the incoming request by validating the password reset token and extracting company information.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param filterChain the filter chain to continue processing the request
	 * @throws ServletException if there is an error during request processing
	 * @throws IOException if there is an error during input/output operations
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		createTokenAndValidate(request, response, filterChain);
	}

	/**
	 * Extracts and validates the password reset token from the request, checks the associated company information,
	 * and continues with the filter chain if no errors are found.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param filterChain the filter chain to continue processing the request
	 * @throws IOException if there is an error during input/output operations
	 * @throws ServletException if there is an error during request processing
	 */
	private void createTokenAndValidate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		AtomicBoolean isException = new AtomicBoolean(false);
		forgotPasswordFilter(request, response, isException);
		if (!isException.get()) {
			doFilterChain(request, response, filterChain);
		}
	}

	/**
	 * Validates the password reset token and the associated company unique code. If valid, it sets the company ID and tenant 
	 * in the request attributes and configures the tenant context. If invalid, it sends an error response.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param isException a flag indicating if an exception has occurred during processing
	 * @throws IOException if there is an error during input/output operations
	 */
	private void forgotPasswordFilter(HttpServletRequest request, HttpServletResponse response, AtomicBoolean isException) throws IOException {
		if ((StringUtils.contains(request.getRequestURI(), ApiPathConstant.RESET_PASSWORD) || StringUtils.contains(request.getRequestURI(), ApiPathConstant.VALIDATE_TOKEN)) && !isException.get()) {
			String token = request.getParameter("token");
			String uniqueCode = jwtUtil.extractUniqueCode(token);

			if (Objects.isNull(token)) {
				sendResponse(response, messageService.getMessage(MessagesConstant.UNIQUE_CODE_MISSING), isException, null);
			} else if (companyService.existsByUniqueCode(uniqueCode)) {
				Optional<CompanyDto> company = companyService.findByByUniqueCode(uniqueCode);
				if (company.isEmpty()) {
					sendResponse(response, messageService.getMessage(MessagesConstant.USERNAME_OR_PASSWORD_INCORRECT), isException, null);
				} else {
					request.setAttribute(Constants.COMPANY_ID, company.get().getId().toString());
					request.setAttribute(Constants.TENANT, company.get().getTenant().toString());
					TenantContext.setCurrentTenant(company.get().getTenant().toString());
				}
			} else {
				sendResponse(response, messageService.getMessage(MessagesConstant.USERNAME_OR_PASSWORD_INCORRECT), isException, null);
			}
		}
	}

	/**
	 * Sends an error response with the specified message and status.
	 * 
	 * @param response the HTTP response to be sent
	 * @param message the message to send in the response
	 * @param isException a flag indicating if an exception has occurred during processing
	 * @param o any additional object to include in the response body
	 * @throws IOException if there is an error during input/output operations
	 */
	private void sendResponse(HttpServletResponse response, String message, AtomicBoolean isException, Object o) throws IOException {
		isException.set(true);
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(new JSONObject(RequestResponseUtils.generateResponseDto(o, message, HttpStatus.UNAUTHORIZED)).toString());
	}

	/**
	 * Continues with the next filter in the filter chain.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param filterChain the filter chain to continue processing the request
	 * @throws IOException if there is an error during input/output operations
	 * @throws ServletException if there is an error during request processing
	 */
	private void doFilterChain(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}

}