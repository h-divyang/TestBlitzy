package com.catering.bean;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Implementation of UserDetails
 * 
 * <p>
 * {@link User} store user information which is later encapsulated into {@link Authentication}
 * objects. This allows non-security related user information (such as email addresses,
 * telephone numbers etc) to be stored in a convenient location.
 * 
 * <p>
 * Here is providing tenant field with {@link UserDetails}
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User implements UserDetails {

	/**
	 * The serialVersionUID is a unique identifier used during the serialization and deserialization process 
	 * of Java objects to ensure that the class definition is compatible with previously serialized instances.
	 * 
	 * This ensures that the current version of the `User` class can be serialized and deserialized correctly.
	 */
	private static final long serialVersionUID = -8198773259698860551L;

	/**
	 * The username used to identify the user for authentication.
	 * It is a unique identifier for the user, and it is typically required to be non-null and unique.
	 */
	private String username;

	/**
	 * The password associated with the user account.
	 * This is expected to be stored in a hashed format for security purposes.
	 */
	private String password;

	/**
	 * The unique identifier for the user in the system.
	 * This is typically used to uniquely identify the user within the database or other system contexts.
	 */
	private Long userId;

	/**
	 * A flag indicating whether the user's account is active.
	 * A value of {@code true} means the account is active, and the user can authenticate and perform authorized actions.
	 */
	private Boolean isActive;

	/**
	 * The user's first name in the default language.
	 * This field stores the user's first name as per the default language set for the user.
	 */
	private String firstNameDefaultLang;

	/**
	 * The user's first name in the preferred language.
	 * This field stores the user's first name in their preferred language, which may differ from the default language.
	 */
	private String firstNamePreferLang;
	
	/**
	 * The user's first name in the supportive language.
	 * This field stores the user's first name in an additional language, typically a secondary or supplementary language for the user.
	 */
	private String firstNameSupportiveLang;

	/**
	 * The user's last name in the default language.
	 * This field stores the user's last name as per the default language set for the user.
	 */
	private String lastNameDefaultLang;

	/**
	 * The user's last name in the preferred language.
	 * This field stores the user's last name in their preferred language, which may differ from the default language.
	 */
	private String lastNamePreferLang;

	/**
	 * The user's last name in the supportive language.
	 * This field stores the user's last name in an additional language, typically a secondary or supplementary language for the user.
	 */
	private String lastNameSupportiveLang;

	/**
	 * A collection of authorities (roles or permissions) granted to the user.
	 * These authorities are used by Spring Security to determine what actions the user is authorized to perform.
	 * For example, a user may have roles such as "ADMIN", "USER", or other custom roles.
	 */
	private Collection<? extends GrantedAuthority> authorities;
	
	/**
	 * Returns the collection of authorities (roles/permissions) granted to the user.
	 * This method is required by the {@link UserDetails} interface and is used by Spring Security to perform authorization checks.
	 *
	 * @return a collection of authorities granted to the user.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * Returns the user's password (hashed) as a string.
	 * This method is required by the {@link UserDetails} interface and is used during the authentication process.
	 *
	 * @return the user's password.
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the username of the user.
	 * This method is required by the {@link UserDetails} interface and is used to uniquely identify the user during authentication.
	 *
	 * @return the user's username.
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Returns {@code true} if the user's account has not expired, or {@code false} otherwise.
	 * In this class, the account expiration status is always assumed to be valid, so it always returns {@code true}.
	 *
	 * @return {@code true} if the account has not expired.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Returns {@code true} if the user's account is not locked, or {@code false} otherwise.
	 * In this class, the account lock status is always assumed to be valid, so it always returns {@code true}.
	 *
	 * @return {@code true} if the account is not locked.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Returns {@code true} if the user's credentials have not expired, or {@code false} otherwise.
	 * In this class, the credential expiration status is always assumed to be valid, so it always returns {@code true}.
	 *
	 * @return {@code true} if the credentials have not expired.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Returns {@code true} if the user's account is enabled, or {@code false} otherwise.
	 * In this class, the account is assumed to always be enabled, so it always returns {@code true}.
	 *
	 * @return {@code true} if the account is enabled.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}