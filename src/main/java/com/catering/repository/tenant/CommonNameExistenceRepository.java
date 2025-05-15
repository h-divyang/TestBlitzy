package com.catering.repository.tenant;

/**
 * CommonNameExistenceRepository provides methods for checking the existence
 * of entities based on various name fields in different languages and
 * optional exclusion by ID.
 *
 * This interface is intended to be used as a base repository interface
 * for performing case-insensitive checks on name fields, supporting
 * scenarios where name uniqueness needs to be enforced.
 */
public interface CommonNameExistenceRepository {

	/**
	 * Checks if an entity exists with the specified name in the default language
	 * (case-insensitive) and a different ID.
	 *
	 * @param nameDefaultLang The name of the entity in the default language (case-insensitive) to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return True if an entity exists with the given name (case-insensitive) in
	 *		   the default language and does not match the specified ID; false otherwise.
	 */
	boolean existsByNameDefaultLangIgnoreCaseAndIdNot(String nameDefaultLang, Long id);

	/**
	 * Checks if an entity exists with the specified name in the default language (case-insensitive).
	 *
	 * @param nameDefaultLang The name of the entity in the default language (case-insensitive) to check for existence.
	 * @return True if an entity exists with the specified name (case-insensitive)
	 *		   in the default language; false otherwise.
	 */
	boolean existsByNameDefaultLangIgnoreCase(String nameDefaultLang);

	/**
	 * Checks if an entity exists with the specified preferred language name (case-insensitive) and a different ID.
	 *
	 * @param namePreferLang The name of the entity in the preferred language (case-insensitive) to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return True if an entity exists with the given name (case-insensitive) in the preferred language
	 *         that does not match the specified ID; false otherwise.
	 */
	boolean existsByNamePreferLangIgnoreCaseAndIdNot(String namePreferLang, Long id);

	/**
	 * Checks if an entity exists with the specified preferred language name (case-insensitive).
	 *
	 * @param namePreferLang The name of the entity in the preferred language (case-insensitive) to check for existence.
	 * @return True if an entity exists with the specified name (case-insensitive) in the preferred language; false otherwise.
	 */
	boolean existsByNamePreferLangIgnoreCase(String namePreferLang);

	/**
	 * Checks if an entity exists with the specified name in the supportive language (case-insensitive) and a different ID.
	 *
	 * @param nameSupportiveLang The name of the entity in the supportive language (case-insensitive) to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return True if an entity exists with the given name (case-insensitive) in the supportive language
	 *		   and does not match the specified ID; false otherwise.
	 */
	boolean existsByNameSupportiveLangIgnoreCaseAndIdNot(String nameSupportiveLang, Long id);

	/**
	 * Checks if an entity exists with the specified name in the supportive language (case-insensitive).
	 *
	 * @param nameSupportiveLang The name of the entity in the supportive language (case-insensitive) to check for existence.
	 * @return True if an entity exists with the given name in the supportive language (case-insensitive); false otherwise.
	 */
	boolean existsByNameSupportiveLangIgnoreCase(String nameSupportiveLang);

}