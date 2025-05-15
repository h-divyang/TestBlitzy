package com.catering.service.common;

import java.util.Map;
import java.util.Optional;
import com.catering.bean.User;
import com.catering.dto.tenant.request.CompanyDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * The JwtService interface provides methods for generating, validating, and extracting
 * information from JWT tokens. It enables token creation with specific claims, tenant
 * information, unique codes, and user details. Additionally, it offers functionality for
 * extracting user and tenant-specific data embedded in the token.
 */
public interface JwtService {

	/**
	 * Extracts the user name from the provided JWT token.
	 *
	 * @param token The JWT token from which the user name will be extracted.
	 * @return The user name embedded in the provided token.
	 */
	String extractUsername(String token);

	/**
	 * Extracts the tenant identifier from the provided JWT token.
	 *
	 * @param token The JWT token from which the tenant identifier will be extracted.
	 * @return The tenant identifier embedded in the provided token.
	 */
	String extractTenant(String token);

	/**
	 * Extracts a unique code from the provided JWT token.
	 *
	 * @param token The JWT token from which the unique code will be extracted.
	 * @return The unique code embedded in the provided token.
	 */
	String extractUniqueCode(String token);

	/**
	 * Extracts the user ID from the provided JWT token.
	 *
	 * @param token The JWT token from which the user ID will be extracted.
	 * @return The user ID embedded in the provided token as a Long value.
	 */
	Long extractUserId(String token);

	/**
	 * Generates a JWT token based on the provided user details, tenant, and unique code.
	 *
	 * @param userDetails The user details used to create the token, including authentication attributes.
	 * @param tenant The tenant identifier for which the token is generated.
	 * @param uniqueCode A unique code to be embedded in the token for additional identification.
	 * @return A JWT token as a String.
	 */
	String generateToken(User userDetails, String tenant, String uniqueCode);

	/**
	 * Generates a token for the "Forgot Password" feature, embedding user and tenant-specific information along with a unique code and expiration time.
	 *
	 * @param userDetails The user details used to create the token, containing authentication attributes.
	 * @param tenant The tenant identifier for which the token is generated.
	 * @param uniqueCode A unique code to be embedded in the token for additional identification.
	 * @param time The expiration time for the token in milliseconds.
	 * @return A JWT token as a String, specifically created for the "Forgot Password" functionality.
	 */
	String generateTokenForgotPassword(User userDetails, String tenant, String uniqueCode, Long time);

	/**
	 * Generates a JWT token based on the provided company information.
	 *
	 * @param company An Optional containing a CompanyDto object that provides tenant and unique code information.
	 * @return A JWT token as a String incorporating the provided company details if available.
	 */
	String generateToken(Optional<CompanyDto> company);

	/**
	 * Creates a JWT token using the provided claims and subject information.
	 *
	 * @param claims The map of claims to be included in the token, representing its payload attributes.
	 * @param subject The subject of the token, typically identifying the principal or user.
	 * @return A JWT token as a String containing the embedded claims and subject.
	 */
	String createToken(Map<String, Object> claims, String subject);

	/**
	 * Creates a JWT token using the provided claims object.
	 *
	 * @param claims The claims to be included in the token, representing its payload attributes.
	 * @return A JWT token as a string containing the embedded claims.
	 */
	String createToken(Claims claims);

	/**
	 * Validates the provided JWT token against the user details.
	 *
	 * @param token The JWT token to be validated.
	 * @param userDetails The user details used for validation, including authentication attributes.
	 * @return A boolean value indicating whether the token is valid (true) or invalid (false).
	 */
	Boolean validateToken(String token, User userDetails);

	/**
	 * Extracts all claims from the provided JWT token.
	 *
	 * @param token The JWT token from which all claims will be extracted.
	 * @return A Jws<Claims> object containing all claims embedded in the provided token.
	 */
	Jws<Claims> extractAllClaims(String token);

}