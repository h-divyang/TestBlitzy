package com.catering.service.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.catering.bean.User;
import com.catering.dto.common.RoleDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.enums.RoleEnum;
import com.catering.service.tenant.CompanyUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service class responsible for handling authentication operations.
 * This class implements the UserDetailsService interface, which is used
 * by Spring Security to retrieve user details for authentication and authorization purposes.
 *
 * The class depends on the CompanyUserService for retrieving company user data
 * and mapping it to Spring Security's UserDetails format.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements UserDetailsService {

	/**
	 * Manages company user-related operations.
	 */
	CompanyUserService companyUserService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<CompanyUserRegistrationDto> companyUserDto = companyUserService.findByUsername(username);
		if (companyUserDto.isPresent()) {
			RoleDto role = RoleEnum.ROLES.stream().filter(_role -> _role.getId().equals(companyUserDto.get().getDesignationId())).findFirst().orElse(null);
			List<String> authorities = new ArrayList<>();
			authorities.add(Objects.nonNull(role) ? role.getRole() : null);
			CompanyUserRegistrationDto companyUser = companyUserDto.get();
			return User.builder()
				.username(companyUser.getUsername())
				.password(companyUser.getPassword())
				.firstNameDefaultLang(companyUser.getFirstNameDefaultLang())
				.firstNamePreferLang(companyUser.getFirstNamePreferLang())
				.firstNameSupportiveLang(companyUser.getFirstNameSupportiveLang())
				.lastNameDefaultLang(companyUser.getLastNameDefaultLang())
				.lastNamePreferLang(companyUser.getLastNamePreferLang())
				.lastNameSupportiveLang(companyUser.getLastNameSupportiveLang())
				.authorities(authorities.stream().map(SimpleGrantedAuthority::new).toList())
				.userId(companyUser.getId())
				.isActive(companyUser.getIsActive())
				.build();
		}
		return null;
	}

}