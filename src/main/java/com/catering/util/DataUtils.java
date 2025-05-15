package com.catering.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.catering.bean.User;
import com.catering.interfaces.Audit;
import com.catering.model.audit.AuditBaseModel;
import com.catering.model.audit.AuditByIdModel;
import com.catering.model.tenant.CompanyUserModelForAudit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for handling common operations related to audit fields, 
 * localization, and user context. 
 * 
 * <p>This class provides methods to set audit fields (e.g., `createdAt`, 
 * `updatedAt`, `createdBy`) for entities, retrieve the current auditor 
 * from the security context, and handle localization preferences such 
 * as language type and code. It also includes helper methods for 
 * validating and setting default language settings.</p>
 * 
 * <p>The utility methods in this class are intended for use in services 
 * and repositories where audit and localization requirements are common.</p>
 * 
 * <b>Key Features:</b>
 * <ul>
 *   <li>Set audit fields for different types of models.</li>
 *   <li>Retrieve the current authenticated user as an auditor.</li>
 *   <li>Validate and determine language type and code with locale support.</li>
 *   <li>Support multiple overloads for different model types.</li>
 * </ul>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataUtils {

	/**
	 * Sets audit fields (e.g., `createdAt`, `updatedAt`, `createdBy`) for the given model.
	 * 
	 * <p>If the provided ID is not null, the existing entity is fetched from the database 
	 * to retain its original `createdAt` and `createdBy` values. For new entities, 
	 * `createdAt` is set to the current time and `editCount` is initialized to 0.</p>
	 * 
	 * @param <M>        The type of the model that extends {@code AuditByIdModel}.
	 * @param <I>        The type of the entity ID (e.g., {@code Long}, {@code UUID}).
	 * @param repository The JPA repository used to fetch the existing entity.
	 * @param id         The ID of the entity. If null, a new entity is assumed.
	 * @param model      The entity model to update.
	 * @return The updated model with audit fields set.
	 */
	public static <M extends AuditByIdModel, I> M setAuditFields(JpaRepository<M, I> repository, I id, M model) {
		if (Objects.nonNull(id)) {
			Optional<M> m = repository.findById(id);
			if (m.isPresent()) {
				M databaseModel = m.get();
				model.setEditCount(databaseModel.getEditCount() + 1);
				model.setCreatedAt(databaseModel.getCreatedAt());
				model.setCreatedBy(databaseModel.getCreatedBy());
			}
		}
		model.setUpdatedAt(LocalDateTime.now());
		Optional<CompanyUserModelForAudit> auditor = getCurrentAuditor();
		if (Objects.isNull(id)) {
			model.setCreatedAt(LocalDateTime.now());
			model.setEditCount(0);
		}
		if (auditor.isPresent()) {
			model.setUpdatedBy(auditor.get());
			if (Objects.isNull(id)) {
				model.setCreatedBy(auditor.get());
			}
		}
		return model;
	}

	/**
	 * Sets audit fields (`createdAt`, `updatedAt`, `editCount`) for an entity that extends {@code AuditBaseModel}.
	 *
	 * <p>If the entity ID is not null, the existing entity is fetched from the repository to retain
	 * the original `createdAt` value and increment the `editCount`. For new entities (when ID is null),
	 * `createdAt` is set to the current time, and `editCount` remains unset (or zero).</p>
	 *
	 * @param <M>        The type of the model extending {@code AuditBaseModel}.
	 * @param <I>        The type of the ID (e.g., {@code Long}, {@code UUID}).
	 * @param repository The JPA repository used to fetch the existing entity.
	 * @param id         The ID of the entity. If null, a new entity is assumed.
	 * @param model      The entity model to update with audit fields.
	 * @return The updated model with audit fields populated.
	 */
	public static <M extends AuditBaseModel, I> M setAuditFields(JpaRepository<M, I> repository, I id, M model) {
		if (Objects.nonNull(id)) {
			Optional<M> m = repository.findById(id);
			if (m.isPresent()) {
				M databaseModel = m.get();
				model.setEditCount(databaseModel.getEditCount() + 1);
				model.setCreatedAt(databaseModel.getCreatedAt());
			}
		} else {
			model.setCreatedAt(LocalDateTime.now());
		}
		model.setUpdatedAt(LocalDateTime.now());
		return model;
	}

	/**
	 * Sets audit fields (`createdAt`, `updatedAt`, `editCount`, `createdById`, `updatedById`) for an entity
	 * that extends {@code Audit}.
	 *
	 * <p>If the entity ID is not null, the existing entity is fetched from the repository to retain
	 * original values for `createdAt`, `createdById`, and `editCount` is incremented. For new entities
	 * (when ID is null), `createdAt` is set to the current time, `editCount` is initialized to zero,
	 * and the `createdById` is set to the current auditor (if available).</p>
	 *
	 * @param <M>        The type of the model extending {@code Audit}.
	 * @param <I>        The type of the ID (e.g., {@code Long}, {@code UUID}).
	 * @param repository The JPA repository used to fetch the existing entity.
	 * @param id         The ID of the entity. If null, a new entity is assumed.
	 * @param model      The entity model to update with audit fields.
	 * @return The updated model with audit fields populated.
	 */
	public static <M extends Audit, I> M setAuditFields(JpaRepository<M, I> repository, I id, M model) {
		if (Objects.nonNull(id)) {
			Optional<M> m = repository.findById(id);
			if (m.isPresent()) {
				M databaseModel = m.get();
				model.setEditCount(databaseModel.getEditCount() + 1);
				model.setCreatedAt(databaseModel.getCreatedAt());
				model.setCreatedById(databaseModel.getCreatedById());
			}
		}
		model.setUpdatedAt(LocalDateTime.now());
		Optional<CompanyUserModelForAudit> auditor = getCurrentAuditor();
		if (Objects.isNull(id)) {
			model.setCreatedAt(LocalDateTime.now());
			model.setEditCount(0);
		}
		if (auditor.isPresent()) {
			model.setUpdatedById(auditor.get().getId());
			if (Objects.isNull(id)) {
				model.setCreatedById(auditor.get().getId());
			}
		}
		return model;
	}

	/**
	 * Retrieves the current authenticated user from the security context and maps 
	 * it to a {@code CompanyUserModelForAudit}.
	 * 
	 * <p>If no user is authenticated or a class cast exception occurs, an empty 
	 * {@code Optional} is returned.</p>
	 * 
	 * @return An {@code Optional} containing the current auditor, or empty if unavailable.
	 */
	public static Optional<CompanyUserModelForAudit> getCurrentAuditor() {
		try {
			Optional<User> user = Optional.ofNullable(SecurityContextHolder.getContext())
					.map(SecurityContext::getAuthentication).filter(Authentication::isAuthenticated)
					.map(Authentication::getPrincipal).map(User.class::cast);
			if (user.isPresent()) {
				return Optional.of(CompanyUserModelForAudit.getInstanceOf(user.get()));
			}
		} catch (ClassCastException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}

	/**
	 * Validates the provided language type and ensures it is one of the supported types.
	 * 
	 * <p>Supported language types:
	 * <ul>
	 *   <li>0 - Default language</li>
	 *   <li>1 - Preferred language</li>
	 *   <li>2 - Supportive language</li>
	 * </ul>
	 * If the provided type is null or invalid, 0 (default language) is returned.</p>
	 * 
	 * @param langType The language type to validate.
	 * @return The validated language type, or 0 if invalid.
	 */
	public static Integer getLangType(Integer langType) {
		if (Objects.isNull(langType) || (langType != 0 && langType != 1 && langType != 2)) {
			return 0;
		}
		return langType;
	}

	/**
	 * Retrieves the {@code Locale} corresponding to the provided language code.
	 * 
	 * <p>If the language code is invalid or not supported, the system's default 
	 * {@code Locale} is returned.</p>
	 * 
	 * @param langCode The language code to resolve (e.g., "en", "hi").
	 * @return The resolved {@code Locale}, or the default {@code Locale} if invalid.
	 */
	public static Locale getLangCode(String langCode) {
		try {
			return Locale.lookup(LanguageRange.parse(langCode), getLocales());
		} catch (Exception e) {
			return Locale.getDefault();
		}
	}

	/**
	 * Provides a list of supported {@code Locale}s for the application.
	 * 
	 * <p>The supported locales include English and several Indian languages 
	 * (e.g., Hindi, Gujarati, Tamil).</p>
	 * 
	 * @return A {@code List} of supported {@code Locale}s.
	 */
	public static List<Locale> getLocales() {
		return Arrays.asList(Locale.ENGLISH, new Locale("hi"), new Locale("gu"), new Locale("te"), new Locale("mr"), new Locale("pa"), new Locale("ml"), new Locale("or"), new Locale("ta"), new Locale("kn"));
	}

}