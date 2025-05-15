package com.catering.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.catering.bean.EmailDetails;
import com.catering.bean.ErrorGenerator;
import com.catering.bean.User;
import com.catering.config.IpAddressConfig;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.EmailConstants;
import com.catering.constant.FileConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.JwtResponseDto;
import com.catering.dto.LoginRequestDto;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.model.tenant.LoginLogModel;
import com.catering.properties.ServerProperties;
import com.catering.repository.tenant.CompanyUserRepository;
import com.catering.repository.tenant.LoginLogRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.common.impl.EmailServiceImpl;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.CompanyUserService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The AuthenticationController class is responsible for managing authentication and authorization-related operations.
 * It provides endpoints for user authentication, password reset operations, token validation, token refresh, and user login logs management.
 * It also manages the setting of the company logo for a specific tenant using caching mechanisms.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = SwaggerConstant.AUTHENTICATION)
public class AuthenticationController {

	/**
	 * Manages the authentication process for users within the application.
	 * Typically utilized for user login, token generation, and related authentication tasks.
	 */
	AuthenticationManager authenticationManager;

	/**
	 * A service used to load user-specific data. 
	 * This is typically used in the context of authentication and authorization, where user details are retrieved and verified during the login process.
	 */
	UserDetailsService userDetailsService;

	/**
	 * Represents a utility service for handling operations related to JSON Web Tokens (JWT).
	 * Provides functionality for extracting claims, generating tokens, and validating tokens.
	 */
	JwtService jwtUtil;

	/**
	 * An instance of the ExceptionService interface utilized within the AuthenticationController to handle and throw custom REST exceptions.
	 * Provides functionality to throw exceptions representing various HTTP status codes.
	 */
	ExceptionService exceptionService;

	/**
	 * Represents a service used for retrieving localized messages.
	 */
	MessageService messageService;

	/**
	 * Holds the server configuration properties such as server URLs and frontend configurations for both local and production environments.
	 */
	ServerProperties serverProperties;

	/**
	 * A service responsible for handling file operations such as uploading files, retrieving file information,
	 * constructing file paths, and generating URLs for files.
	 */
	FileService fileService;

	/**
	 * Repository interface for handling database operations related to login logs.
	 * Acts as a bridge between the application and the data source for managing user login attempt records.
	 */
	LoginLogRepository loginLogRepository;

	/**
	 * Manages access to and operations on company user-related data within the application.
	 */
	CompanyUserRepository companyUserRepository;

	/**
	 * Manages and coordinates caching functionalities within the application.
	 * Used to store, retrieve, and manage cached data to optimize performance and reduce redundant operations or calculations.
	 */
	CacheManager cacheManager;

	/**
	 * Service layer interface for managing and performing operations related to company users.
	 * This field is used for operations such as updating user details, managing passwords, handling user registration, sending verification emails, and retrieving user information.
	 */
	CompanyUserService companyUserService;

	/**
	 * Service responsible for handling operations related to company preferences.
	 * Provides methods for retrieving, updating, saving, and checking the status of company preferences.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * An instance of the EmailServiceImpl class that provides functionalities to send emails either as simple text emails or emails with a template design.
	 */
	EmailServiceImpl emailServiceImpl;

	/**
	 * Authenticates the user based on the provided credentials and generates a JWT token for the successfully authenticated user.
	 *
	 * @param uniqueCode A unique code identifying the company associated with the login request.
	 * @param loginDto The login request data containing the username and password.
	 * @param request The HTTP servlet request containing additional authentication-related attributes.
	 * @return A ResponseContainerDto containing a JwtResponseDto with the generated JWT token and user details upon successful authentication.
	 */
	@PostMapping(value = ApiPathConstant.AUTHENTICATE)
	public ResponseContainerDto<JwtResponseDto> authenticate(@RequestHeader(value="companyUniqueCode") String uniqueCode, @RequestBody LoginRequestDto loginDto, HttpServletRequest request) {
		if (!Boolean.TRUE.equals(companyPreferencesService.isActive())) {
			Map<String, Boolean> errorMap = new HashMap<>();
			errorMap.put("isAccessDenied", true);
			exceptionService.throwRestException(HttpStatus.OK, messageService.getMessage(MessagesConstant.ACCESS_DENIED), errorMap);
		}

		/**
		 * Get {@link User} by username if it's exist
		 * 
		 * @throws UsernameNotFoundException if the user could not be found or the user has no
		 * GrantedAuthority
		 * */
		User userDetails = new User();
		userDetails.setUsername(loginDto.getUsername());
		try {
			userDetails = (User) userDetailsService.loadUserByUsername(loginDto.getUsername());

			/**
			 * Create UsernamePasswordAuthenticationToken based on username and password which is given by User
			 * */
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

			/**
			 * Get {@link User} by username if it's exist
			 * 
			 * @throws UsernameNotFoundException if the user could not be found or the user has no
			 * GrantedAuthority
			 * */
			Object tenant = request.getAttribute(Constants.TENANT);
			if (Objects.nonNull(tenant) && (Boolean.FALSE.equals(userDetails.getIsActive()))) {
				ErrorGenerator error = ErrorGenerator.builder()
						.putError("isActive", userDetails.getIsActive())
						.putError("link", serverProperties.getRootUrl() + ApiPathConstant.COMPANY_USER + ApiPathConstant.SEND_VERIFICATION_EMAIL + "/" + userDetails.getUserId() + "/" + tenant.toString());
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.JWT_BAD_CREDENTIALS), error.getErrors());
			}

			/**
			 * Create JWT from the {@link User}
			 * */
			JwtResponseDto jwtResponseDto = JwtResponseDto.builder()
				.token(jwtUtil.generateToken(userDetails, Objects.nonNull(tenant) ? tenant.toString() : null, uniqueCode))
				.firstNameDefaultLang(userDetails.getFirstNameDefaultLang())
				.firstNamePreferLang(userDetails.getFirstNamePreferLang())
				.firstNameSupportiveLang(userDetails.getFirstNameSupportiveLang())
				.lastNameDefaultLang(userDetails.getLastNameDefaultLang())
				.lastNamePreferLang(userDetails.getLastNamePreferLang())
				.lastNameSupportiveLang(userDetails.getLastNameSupportiveLang())
				.id(userDetails.getUserId())
				.uniqueCode(uniqueCode)
				.avtar(fileService.getUrl(FileConstants.MODULE_USER_PROFILE, userDetails.getUserId().toString()))
				.build();

			setCurrentCompanyLogo(tenant);

			// Authentication successful
			saveLoginLog(userDetails, true, request);

			return  RequestResponseUtils.generateResponseDto(jwtResponseDto);
		} catch (BadCredentialsException ex) {
			// Authentication failed
			if (userDetails.getUsername() != null) {
				saveLoginLog(userDetails, false, request);
			}
			return null; // or handle the response accordingly
		} catch (UsernameNotFoundException ex) {
			// Authentication failed
			saveLoginLog(userDetails, false, request);
			return null; // or handle the response accordingly
		}
	}

	/**
	 * Handles password reset requests by generating a reset token and sending a password reset email to the user.
	 *
	 * @param uniqueCode A unique code identifying the company associated with the request.
	 * @param loginDto The login request data containing the username of the user who forgot their password.
	 * @param request The HTTP servlet request, used to extract additional attributes, e.g., tenant information.
	 * @return A ResponseContainerDto containing a message indicating whether the password reset email was sent successfully or an error occurred.
	 */
	@PostMapping(value = ApiPathConstant.AUTHENTICATE + ApiPathConstant.FORGOT_PASSWORD)
	public ResponseContainerDto<String> forgotPassword(@RequestHeader(value="companyUniqueCode") String uniqueCode,  @RequestBody LoginRequestDto loginDto, HttpServletRequest request) {
		Object tenant = request.getAttribute(Constants.TENANT);
		try {
			Optional<CompanyUserRegistrationDto> companyUser = companyUserService.findByUsername(loginDto.getUsername());
			User userDetails = (User) userDetailsService.loadUserByUsername(loginDto.getUsername());
			Long time = 1 * 24 * 60 * 60 * 1000L;
			String token = jwtUtil.generateTokenForgotPassword(userDetails, Objects.nonNull(tenant) ? tenant.toString() : null, uniqueCode, time);
			if (companyUser.isPresent() && companyUser.get().getUsername().equals(loginDto.getUsername()) && Objects.nonNull(companyUser.get().getEmail())) {
				Map<String, Object> properties = new HashMap<>();
				properties.put(EmailConstants.RESET_PASSWORD_LINK, serverProperties.getFrontendUrl() + "/#/authenticate" + ApiPathConstant.RESET_PASSWORD + "?token=" + token);
				EmailDetails emailDetails = EmailDetails.builder()
						.to(companyUser.get().getEmail())
						.subject(EmailConstants.SUBJECT_RESET_PASSWORD)
						.templet(EmailConstants.TEMPLATE_RESET_PASSWORD)
						.properties(properties)
						.build();
				emailServiceImpl.sendMailTemplet(emailDetails);
				CompanyUserModel companyUserModel = companyUserRepository.getById(companyUser.get().getId());
				companyUserModel.setResetPasswordToken(token);
				companyUserRepository.save(companyUserModel);
				return RequestResponseUtils.generateResponseDto(messageService.getMessage(MessagesConstant.REST_RESPONSE_SEND_RESET_PASSWORD_EMAIL));
			} else if (!companyUser.isPresent()) {
				return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.USERNAME_OR_PASSWORD_INCORRECT), HttpStatus.BAD_REQUEST);
			} else if (!Objects.nonNull(companyUser.get().getEmail())) {
				return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.EMAIL_NOT_EXIST), HttpStatus.BAD_REQUEST);
			} else {
				return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.USERNAME_OR_PASSWORD_INCORRECT), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.USERNAME_OR_PASSWORD_INCORRECT), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Resets the user's password using a provided token and the new password.
	 * If the operation is successful, a success message is returned, otherwise an error message.
	 *
	 * @param token The token used to verify the password reset request.
	 * @param newPassword The new password provided by the user for the reset.
	 * @param request The HTTP servlet request, containing contextual request details.
	 * @param response The HTTP servlet response for sending responses back to the client.
	 * @return A ResponseContainerDto containing a success message if the password is successfully reset, or an error message if the operation fails.
	 */
	@PostMapping(value = ApiPathConstant.RESET_PASSWORD)
	public ResponseContainerDto<String> resetPassword(@RequestParam String token, @RequestParam String newPassword, HttpServletRequest request, HttpServletResponse response) {
		ResponseContainerDto<String> passwordReset = companyUserService.resetPassword(token, newPassword);
		if(passwordReset.getStatus().toString().equals("200")) {
			return RequestResponseUtils.generateResponseDto(messageService.getMessage(MessagesConstant.REST_RESPONSE_RESET_PASSWORD_SUCCESSFUL));
		}
		return passwordReset;
	}

	/**
	 * Validates the provided token to check if it is expired or invalid.
	 *
	 * @param token The JWT token to validate.
	 * @return A ResponseEntity containing a map with a single entry: "isExpired" as the key and a boolean value indicating if the token is expired (true) or valid (false).
	 */
	@GetMapping(value = ApiPathConstant.VALIDATE_TOKEN)
	public ResponseEntity<Map<String, Boolean>> validateToken(@RequestParam String token) {
		boolean isExpired;
		try {
			isExpired = jwtUtil.validateToken(token, null);
		} catch (Exception e) {
			isExpired = true;
		}
		String username = jwtUtil.extractUsername(token);
		Optional<CompanyUserRegistrationDto> companyUserDto = companyUserService.findByUsername(username);
		if (!companyUserDto.isEmpty() && (!Objects.nonNull(companyUserDto.get().getResetPasswordToken()) || !companyUserDto.get().getResetPasswordToken().equals(token))) {
			isExpired = true;
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put("isExpired", isExpired);
		return ResponseEntity.ok(response);
	}

	/**
	 * Refreshes the JWT token if the provided token in the request is valid and has not yet expired.
	 * Generates and returns a new JWT token based on the claims from the current token.
	 * If the token is not eligible for refresh, an error response is returned.
	 *
	 * @param request The HTTP servlet request containing the current JWT token claims, retrieved as an attribute titled "claims".
	 * @return A ResponseContainerDto containing:
	 *		A JwtResponseDto with the refreshed JWT token if the token is eligible for renewal.
	 *		An error message and a BAD_REQUEST status if the token is not yet expired or invalid.
	 */
	@GetMapping(value = ApiPathConstant.REFRESH_TOKEN)
	public ResponseContainerDto<Object> refreshToken(HttpServletRequest request) {
		Claims claims = (Claims) request.getAttribute("claims");
		Optional<Map<String, Object>> optionalExpectedMap = getMapFromTokenClaims(claims);

		if (optionalExpectedMap.isPresent()) {
			Map<String, Object> expectedMap = optionalExpectedMap.get();
			return RequestResponseUtils.generateResponseDto(JwtResponseDto.builder().token(jwtUtil.createToken(expectedMap, expectedMap.get("sub").toString())).build());
		} else {
			return RequestResponseUtils.generateResponseDto(ErrorGenerator.builder().putError("isTokenInvalid", false).getErrors(), messageService.getMessage(MessagesConstant.TOKEN_IS_NOT_EXIRED_YET), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Converts the claims from a JWT token into a map of key-value pairs.
	 * If the provided claims are null, an empty Optional is returned.
	 *
	 * @param claims The token claims to be converted into a map of key-value pairs.
	 * @return An Optional containing the map representation of the claims if they are not null, or an empty Optional if the claims are null.
	 */
	private Optional<Map<String, Object>> getMapFromTokenClaims(Claims claims) {
		if (Objects.isNull(claims)) {
			return Optional.empty();
		}
		Map<String, Object> expectedMap = new HashMap<>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return Optional.of(expectedMap);
	}

	/**
	 * Saves a log detailing a user's login attempt, recording information such as the user ID, login timestamp, login status, IP address, and username.
	 *
	 * @param userDetails The user details object containing information about the user attempting to log in.
	 * @param loginSuccess A boolean indicating whether the login attempt was successful.
	 * @param request The HTTP servlet request used to retrieve the client's IP address.
	 */
	private void saveLoginLog(User userDetails, boolean loginSuccess, HttpServletRequest request) {
		LoginLogModel loginLog = new LoginLogModel();
		String username = userDetails.getUsername();
		try {
			CompanyUserModel companyUser = companyUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("CompanyUser not found for username: " + username));
			loginLog.setUserId(companyUser.getId());
		} catch (UsernameNotFoundException e) {
			loginSuccess = false;
		}

		loginLog.setLoginTimeStamp(LocalDateTime.now());
		loginLog.setStatus(loginSuccess);
		String ipAddress = IpAddressConfig.getClientIpAddress(request);
		loginLog.setIpAddress(ipAddress); // Set the IP address
		loginLog.setUsername(userDetails.getUsername());

		loginLogRepository.save(loginLog);
	}

	/**
	 * Sets the current company logo for the given tenant by retrieving the logo from the file service and caching it if not already present in the cache.
	 *
	 * @param tenant The object representing the tenant for which the company logo needs to be set in the cache.
	 */
	public void setCurrentCompanyLogo(Object tenant) {
		Cache cache = cacheManager.getCache(Constants.COMPANY_LOGO_CACHE);
		if (Objects.nonNull(cache) && (Objects.isNull(cache.get(tenant.toString() + "-" + Constants.LOGO)))) {
			FileUtils.getCompanyLogoAndSetInCache(fileService, cache, tenant);
		}
	}

}