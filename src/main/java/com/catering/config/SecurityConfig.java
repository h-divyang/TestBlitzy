package com.catering.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.MessagesConstant;
import com.catering.filter.ForgotPasswordFilter;
import com.catering.filter.JwtFilter;
import com.catering.service.common.MessageService;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Security configuration class for setting up Spring Security, CORS, and authentication mechanisms.
 * <p>
 * This class configures security settings for the application, including authentication, authorization,
 * session management, and exception handling. It uses JWT for stateless authentication and configures
 * CORS (Cross-Origin Resource Sharing) settings to allow specific origins, methods, and headers. It also
 * sets up custom filters for JWT-based authentication and forgot password functionality.
 * </p>
 * 
 * <ul>
 *   <li>Disables CSRF (Cross-Site Request Forgery) protection for stateless REST APIs.</li>
 *   <li>Defines authorization rules for different API paths and roles (e.g., SUPERADMIN).</li>
 *   <li>Uses JWT for stateless authentication by adding a custom JwtFilter.</li>
 *   <li>Handles authentication exceptions and returns appropriate JSON responses.</li>
 *   <li>Configures CORS to allow cross-origin requests from specific origins, methods, and headers.</li>
 * </ul>
 * 
 * @see JwtFilter
 * @see ForgotPasswordFilter
 * @see AuthenticationManager
 * @see BCryptPasswordEncoder
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

	MessageService messageService;

	/**
     * Configures HTTP security settings for the application.
     * <p>
     * This method disables CSRF protection (because the application is stateless and only uses token-based authentication),
     * sets up authorization rules, and configures session management to be stateless. It also registers custom filters
     * like {@link JwtFilter} for JWT-based authentication and {@link ForgotPasswordFilter} for handling forgot password requests.
     * </p>
     * 
     * @param http the {@link HttpSecurity} object to configure security settings
     * @param jwtFilter the JWT filter to intercept and authenticate requests
     * @param forgotPasswordFilter the filter to handle forgot password requests
     * @return a {@link SecurityFilterChain} object for Spring Security configuration
     * @throws Exception if an error occurs during configuration
     */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter, ForgotPasswordFilter forgotPasswordFilter) throws Exception {
		http.cors().and().csrf().disable().authorizeHttpRequests()

			/**
			 * Permit all the request which is mentioned here
			 * */
			.antMatchers(ApiPathConstant.getAllowRequests()).permitAll()

			/**
			 * Allow all the master APIs, only allow get the data
			 * */
			.antMatchers(HttpMethod.GET, ApiPathConstant.getAllowMasterRequests()).permitAll()

			/**
			 * Allow all the superadmin requests who have authority SUPERADMIN
			 * */
			.antMatchers(ApiPathConstant.SUPERADMIN + "/*").hasAuthority("SUPERADMIN")
			.antMatchers("/view/**").permitAll()
			/**
			 * All requests will authenticate excluding permitted requests
			 * */
			.anyRequest().authenticated()
			.and()
			/**
			 * Spring Security will never create an {@link HttpSession} and it will never use it
			 * */
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		/**
		 * Add exception handler to handle any exception occur when any request doesn't follow {@link SecurityFilterChain} configuration
		 * */
		http.exceptionHandling().authenticationEntryPoint((request, response, e) -> sendResponse(response, e));

		/**
		 * Add {@link JwtFilter} to filter all the request before authenticate
		 * */
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(forgotPasswordFilter, jwtFilter.getClass());

		return http.build();
	}

	/**
	 * Sends a formatted JSON response when an authentication exception occurs.
	 * <p>
	 * This method constructs a response with an appropriate message and HTTP status
	 * based on the exception type (e.g., unauthorized access or internal server error).
	 * </p>
	 * 
	 * @param response the {@link HttpServletResponse} object to write the response
	 * @param e the exception that triggered the error
	 * @throws IOException if an error occurs while writing the response
	 */
	private void sendResponse(HttpServletResponse response, Exception e) throws IOException {
		String msg = null;
		HttpStatus status = null;
		if (e instanceof InsufficientAuthenticationException) {
			msg = messageService.getMessage(MessagesConstant.REST_REQUEST_ACCESS_DENIED);
			status = HttpStatus.UNAUTHORIZED;
		} else {
			msg = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new JSONObject(RequestResponseUtils.generateResponseDto(null, msg, status)).toString());
	}

	/**
	 * Creates and configures the {@link AuthenticationManager} bean.
	 * <p>
	 * This bean is responsible for managing the authentication process and is used by Spring Security
	 * for authenticating users based on their credentials.
	 * </p>
	 * 
	 * @param authConfig the {@link AuthenticationConfiguration} used to get the authentication manager
	 * @return the configured {@link AuthenticationManager} instance
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Creates and configures the {@link BCryptPasswordEncoder} bean.
	 * <p>
	 * This bean is used for securely encoding and decoding passwords in Spring Security.
	 * </p>
	 * 
	 * @return the configured {@link BCryptPasswordEncoder} instance
	 */
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures CORS (Cross-Origin Resource Sharing) settings for the application.
	 * <p>
	 * This method sets up a CORS configuration that allows all origins, credentials, headers,
	 * and specific HTTP methods like GET, POST, PUT, DELETE, PATCH.
	 * </p>
	 * 
	 * @return a {@link CorsConfigurationSource} bean that defines the CORS settings
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Content-Type", "companyUniqueCode", "Authorization"));
		configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PUT", "PATCH"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}