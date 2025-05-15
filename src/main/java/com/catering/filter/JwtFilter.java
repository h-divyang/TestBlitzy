package com.catering.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.catering.bean.User;
import com.catering.config.TenantContext;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.CompanyDto;
import com.catering.properties.DataSourceProperties;
import com.catering.properties.JwtProperties;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.common.impl.AuthenticationService;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.CompanyService;
import com.catering.util.RequestResponseUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * JwtFilter is a custom filter that extends {@link OncePerRequestFilter} and is responsible for:
 * <ul>
 * <li>Extracting and validating JWT tokens from HTTP requests.</li>
 * <li>Setting up authentication in the Spring Security context.</li>
 * <li>Handling token expiration and other JWT-related exceptions.</li>
 * <li>Processing login and user-specific data based on JWT claims.</li>
 * </ul>
 * The filter intercepts incoming HTTP requests and checks for a valid JWT in the header. If valid, it sets
 * up the user authentication context and passes the request further into the application.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtFilter extends OncePerRequestFilter {

	/**
	 * Service responsible for generating and validating JWT tokens.
	 */
	JwtService jwtService;

	/**
	 * Service responsible for handling user authentication and retrieval.
	 */
	AuthenticationService userService;

	/**
	 * Configuration properties related to JWT, such as header name, prefix, etc.
	 */
	JwtProperties jwtProperties;

	/**
	 * Configuration properties for the data source, including database name.
	 */
	DataSourceProperties dataSourceProperties;

	/**
	 * Service for managing application-specific messages.
	 */
	MessageService messageService;

	/**
	 * Service responsible for handling company-related data.
	 */
	CompanyService companyService;

	/**
	 * Service for managing company preferences.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Processes the incoming request by validating the JWT token, extracting user information,
	 * and setting up the Spring Security authentication context.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param filterChain the filter chain to continue processing the request
	 * @throws ServletException if there is an error during request processing
	 * @throws IOException if there is an error during input/output operations
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		createTokenAndValidate(request, response, filterChain);
	}

	/**
	 * Extracts and validates the JWT token from the request header, processes the token,
	 * and sets user authentication information in the request.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param filterChain the filter chain to continue processing the request
	 * @throws IOException if there is an error during input/output operations
	 * @throws ServletException if there is an error during request processing
	 */
	private void createTokenAndValidate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		AtomicBoolean isException = new AtomicBoolean(false);
		final String header = request.getHeader(jwtProperties.getHeader());
		String token = null;
		String username = null;
		String tenant = null;
		Map<String, Object> errorMap = new HashMap<>();
		if (Objects.nonNull(header) && header.startsWith(jwtProperties.getPrefix())) {
			try {
				/**
				 * Get JWT token from request header
				 * */
				token = header.substring(7);
				username = jwtService.extractUsername(token);
				tenant = jwtService.extractTenant(token);

				request.setAttribute(Constants.TENANT, tenant);
				MDC.put(Constants.TENANT, tenant);
				setUserIdToRequest(request, token);
			} catch (ExpiredJwtException e) {
				if (StringUtils.containsIgnoreCase(request.getRequestURI(), ApiPathConstant.REFRESH_TOKEN)) {
					allowForRefreshToken(e, request);
				} else {
					errorMap.put("isTokenExpired", true);
					sendResponse(response, messageService.getMessage(MessagesConstant.JWT_EXPIRED), isException, errorMap);
				}
			} catch (SignatureException | MalformedJwtException e) {
				errorMap.put("isTokenInvalid", true);
				sendResponse(response, messageService.getMessage(MessagesConstant.JWT_INVALID), isException, errorMap);
			} catch (JwtException e) {
				sendResponse(response, e.getMessage(), isException, null);
			} catch (Exception e) {
				sendResponse(response, messageService.getMessage(MessagesConstant.CORE_SOMETHING_WENT_WRONG), isException, null);
			}
		}
		TenantContext.setCurrentTenant(dataSourceProperties.getName());
		setAuthentication(request, response, username, tenant, token, isException);
		updateUserFilter(request, token);
		loginFilter(request, response, isException);
		if (!isException.get()) {
			doFilterChain(request, response, filterChain);
		}
	}

	/**
	 * Extracts the user ID from the JWT token and sets it as a request attribute.
	 * 
	 * @param request the HTTP request being processed
	 * @param token the JWT token containing user information
	 */

	private void setUserIdToRequest(HttpServletRequest request, String token) {
		Long userId = jwtService.extractUserId(token);
		if (Objects.nonNull(userId)) {
			request.setAttribute(Constants.USER_ID, userId);
		}
	}

	/**
	 * Allows for the refresh token process when the JWT has expired.
	 * Sets up the authentication context for token refresh operations.
	 * 
	 * @param ex the exception that caused token expiration
	 * @param request the HTTP request being processed
	 */
	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
		/** create a UsernamePasswordAuthenticationToken with null values.*/
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null, null);
		/**
		 * After setting the Authentication in the context, we specify
		 * that the current user is authenticated. So it passes the
		 * Spring Security Configurations successfully.
		 */

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		/**
		 * Set the claims so that in controller we will be using it to create
		 */
		request.setAttribute("claims", ex.getClaims());
	}

	/**
	 * Validates the JWT token and sets up the user authentication in the Spring Security context.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param username the username extracted from the JWT token
	 * @param tenant the tenant extracted from the JWT token
	 * @param token the JWT token containing user authentication information
	 * @param isException a flag indicating if an exception has occurred during processing
	 * @throws IOException if there is an error during input/output operations
	 */
	private void setAuthentication(HttpServletRequest request, HttpServletResponse response, String username, String tenant, String token, AtomicBoolean isException) throws IOException {
		if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication()) && Objects.nonNull(tenant)) {
			TenantContext.setCurrentTenant(tenant);
			User userDetails = (User) userService.loadUserByUsername(username);
			if (!Boolean.TRUE.equals(companyPreferencesService.isActive())) {
				Map<String, Boolean> errorMap = new HashMap<>();
				errorMap.put("isAccessDenied", true);
				sendResponse(response, messageService.getMessage(MessagesConstant.ACCESS_DENIED), isException, errorMap);
			} else if (Boolean.TRUE.equals(jwtService.validateToken(token, userDetails))) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
	}

	/**
	 * Filters the login request by verifying the company unique code (UCC) and extracting the company ID and tenant information.
	 * 
	 * @param request the HTTP request being processed
	 * @param response the HTTP response to be sent
	 * @param isException a flag indicating if an exception has occurred during processing
	 * @throws IOException if there is an error during input/output operations
	 */
	private void loginFilter(HttpServletRequest request, HttpServletResponse response, AtomicBoolean isException) throws IOException {
		if (StringUtils.contains(request.getRequestURI(), ApiPathConstant.AUTHENTICATE) && !isException.get()) {
			String header = request.getHeader("companyUniqueCode");
			if (Objects.isNull(header)) {
				sendResponse(response, messageService.getMessage(MessagesConstant.UNIQUE_CODE_MISSING), isException, null);
			} else if (companyService.existsByUniqueCode(header)) {
				Optional<CompanyDto> company = companyService.findByByUniqueCode(header);
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
	 * Updates the user filter based on the request URI and HTTP method.
	 * 
	 * @param request the HTTP request being processed
	 * @param token the JWT token
	 */
	private void updateUserFilter(HttpServletRequest request, String token) {
		if (StringUtils.containsAny(request.getRequestURI(), ApiPathConstant.USER) && request.getMethod().equals(HttpMethod.PUT.name())) {
			request.setAttribute(Constants.TOKEN, token);
		}
	}

	/**
	 * Executes the next filter in the filter chain.
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

	/**
	 * Sends a response with the provided message and status.
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

}