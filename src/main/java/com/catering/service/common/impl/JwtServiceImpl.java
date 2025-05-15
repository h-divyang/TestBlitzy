package com.catering.service.common.impl;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.catering.bean.User;
import com.catering.dto.tenant.request.CompanyDto;
import com.catering.properties.JwtProperties;
import com.catering.service.common.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link JwtService} interface for handling JSON Web Tokens (JWT).
 * This class is responsible for creating, parsing, validating, and extracting information from JWTs.
 * It uses cryptographic mechanisms for signing and verifying tokens based on a secret key.
 *
 * The service primarily uses the {@link JwtProperties} configuration for managing token attributes
 * like expiration time, key, and specific claims.
 * 
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {

	/**
	 * Represents the cryptographic key used for signing or verifying JWT tokens.
	 */
	Key key;

	/**
	 * Contains configuration properties for handling JWT operations.
	 */
	JwtProperties jwtProperties;

	/**
	 * Constructor for the JwtServiceImpl class.
	 *
	 * Initializes the service with the provided JwtProperties and generates
	 * a cryptographic key based on the JWT configuration.
	 *
	 * @param jwtProperties The configuration properties for JWT, including
	 *						headers, expiration, secret key, and other related settings.
	 */
	public JwtServiceImpl(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.key = getKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String extractTenant(String token) {
		return (String) extractClaim(token, jwtProperties.getXTenant());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String extractUniqueCode(String token) {
		return (String) extractClaim(token, jwtProperties.getUniqueCode());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long extractUserId(String token) {
		Object userId = extractClaim(token, jwtProperties.getUserId());
		return Objects.isNull(userId) ? null : Long.valueOf(userId.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Jws<Claims> extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateToken(User userDetails, String tenant, String uniqueCode) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(jwtProperties.getXTenant(), tenant);
		claims.put(jwtProperties.getAuthorities(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
		claims.put(jwtProperties.getUserId(), userDetails.getUserId());
		claims.put(jwtProperties.getUniqueCode(), uniqueCode);
		return createToken(claims, userDetails.getUsername());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateTokenForgotPassword(User userDetails, String tenant, String uniqueCode, Long time) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(jwtProperties.getXTenant(), tenant);
		claims.put(jwtProperties.getAuthorities(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
		claims.put(jwtProperties.getUserId(), userDetails.getUserId());
		claims.put(jwtProperties.getUniqueCode(), uniqueCode);
		Date expirationDate = new Date(System.currentTimeMillis() + time);
		claims.put(jwtProperties.getExpireTimeInMillis().toString(), expirationDate);
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(key).compact();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateToken(Optional<CompanyDto> company) {
		if (company.isPresent()) {
			Map<String, Object> claims = new HashMap<>();
			claims.put(jwtProperties.getXTenant(), company.get().getTenant());
			return createToken(claims, null);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpireTimeInMillis()))
				.signWith(key).compact();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean validateToken(String token, User userDetails) {
		final String username = extractUsername(token);
		if(Objects.nonNull(userDetails)) {
			return (userDetails.getUsername().equals(username) && !isTokenExpired(token));
		} else {
			return (isTokenExpired(token));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createToken(Claims claims) {
		return Jwts.builder().setClaims(claims).signWith(key).compact();
	}

	/**
	 * Check token expiration.
	 * 
	 * @param token It will get expiration from.
	 * @return True if expired otherwise false.
	 * */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Extract Expiration from token.
	 * 
	 * @param token It will get Expiration from.
	 * @return {@link Date} Expiration Date.
	 * */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extract any claim from token by passing token and {@link Function} type.
	 * 
	 * @param token It will get claim from.
	 * @param claim {@link Function} Type to get particular claim.
	 * 
	 * @return T type of object.
	 * */
	private <T> T extractClaim(String token, Function<Claims, T> claim) {
		final Jws<Claims> claims = extractAllClaims(token);
		return claim.apply(claims.getBody());
	}

	/**
	 * Extract any claim from token by passing token with key.
	 * 
	 * @param token It will get claim from.
	 * @param key To get particular claim from token.
	 * 
	 * @return {@link Object} Type of claim
	 * */
	private Object extractClaim(String token, String key) {
		final Jws<Claims> claims = extractAllClaims(token);
		return claims.getBody().get(key);
	}

	/**
	 * Generates a cryptographic key using the SHA-256 hash of a secret key obtained from the application's JWT properties.
	 * The resultant key is encoded using Base64 and wrapped into a SecretKeySpec.
	 *
	 * This method is designed to provide a key that can be used with HmacSHA256
	 * for cryptographic operations. If an error occurs while trying to generate
	 * the key, it returns null.
	 *
	 * @return The generated {@link Key} based on the application's JWT properties,
	 * 		   or null if an error occurs during key generation.
	 */
	private Key getKey() {
		try {
			// Use SHA-256 hash function.
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));

			// Encode the hash as Base64 to get a string representation.
			String base64Encoded = Base64.getEncoder().encodeToString(hash);

			// For actual cryptographic use, consider using a KeyGenerator instead of this simplistic approach.
			return new javax.crypto.spec.SecretKeySpec(base64Encoded.getBytes(), "HmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}