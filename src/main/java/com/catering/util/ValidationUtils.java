package com.catering.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.dto.common.RecordExistDto;
import com.catering.dto.tenant.request.CommonMultiLanguageDto;
import com.catering.repository.tenant.CommonNameExistenceRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Provide validation utilities
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

	/**
	 * Check given email is valid or not
	 * 
	 * @param email to be check
	 * @return <code>true</code> if email is valid otherwise <code>false</code>
	 * */
	public static boolean isEmail(String email) {
		return regexChecker(RegexConstant.EMAIL, email);
	}

	/**
	 * Check given text is only numbers or not
	 * 
	 * @param number to be check
	 * @return <code>true</code> if text contains only numbers otherwise <code>false</code>
	 * */
	public static boolean isNumber(String number) {
		return regexChecker(RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, number);
	}

	/**
	 * Check given regex and matches with text
	 * 
	 * @param regex Regular expression
	 * @param matches to be match
	 * @return <code>true</code> if text matches with regex otherwise <code>false</code>
	 * */
	public static boolean regexChecker(String regex, String matches) {
		if (StringUtils.isNoneBlank(regex) && StringUtils.isNotBlank(matches)) {
			return Pattern.matches(regex, matches);
		}
		return false;
	}

	/**
	 * Validates the fields of a CommonMultiLanguageDto object.
	 * <p>Checks if the provided names for default, preferred, and supportive languages already exist in the repository.</p>
	 * 
	 * @param t The CommonMultiLanguageDto object containing the names in various languages.
	 * @param repository The repository to check for existing records.
	 * @param exceptionService Service for handling exceptions.
	 * @param messageService Service for retrieving error messages.
	 * @param <T> The type of the CommonMultiLanguageDto.
	 */
	public static <T extends CommonMultiLanguageDto> void validateFields(T t, CommonNameExistenceRepository repository, ExceptionService exceptionService, MessageService messageService) {
		validateFields(repository, exceptionService, messageService, t.getNameDefaultLang(), t.getNamePreferLang(), t.getNameSupportiveLang(), t.getId());
	}

	/**
	 * Validates the provided language fields and checks for their existence in the repository.
	 * <p>Throws an exception if any name already exists in the repository for the given language fields.</p>
	 * 
	 * @param repository The repository to check for existing records.
	 * @param exceptionService Service for throwing exceptions.
	 * @param messageService Service for retrieving error messages.
	 * @param nameDefaultLang The default language name to be checked.
	 * @param namePreferLang The preferred language name to be checked.
	 * @param nameSupportiveLang The supportive language name to be checked.
	 * @param id The ID of the object being checked (used for excluding the current object when checking).
	 */
	public static void validateFields(CommonNameExistenceRepository repository, ExceptionService exceptionService, MessageService messageService, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long id) {
		boolean isDefaultExist = materialNameExists(repository, nameDefaultLang, Constants.LANGUAGE_DEFAULT, id);
		boolean isPreferExist = StringUtils.isNotBlank(namePreferLang) && materialNameExists(repository, namePreferLang, Constants.LANGUAGE_PREFER, id);
		boolean isSupportiveExist = StringUtils.isNotBlank(nameSupportiveLang) && materialNameExists(repository, nameSupportiveLang, Constants.LANGUAGE_SUPPORTIVE, id);

		if (isDefaultExist || isPreferExist || isSupportiveExist) {
			RecordExistDto recordExistDto = RecordExistDto.builder().isExist(true).isNameDefaultLang(isDefaultExist).isNamePreferLang(isPreferExist).isNameSupportiveLang(isSupportiveExist).build();
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.ALREADY_EXIST), recordExistDto);
		}
	}

	/**
	 * Checks if a material name already exists in the repository for the specified language.
	 * <p>Also ensures that the current object's ID is excluded from the check, if provided.</p>
	 * 
	 * @param commonMultiLanguageNameRepository The repository to check for existing records.
	 * @param name The name to be checked for existence.
	 * @param language The language in which to check the name.
	 * @param id The ID of the object being checked (used for excluding the current object from the check).
	 * @return {@code true} if the name exists in the repository for the given language; otherwise {@code false}.
	 */
	public static boolean materialNameExists(CommonNameExistenceRepository commonMultiLanguageNameRepository, String name, String language, Long id) {
		switch (language) {
		case Constants.LANGUAGE_DEFAULT:
			return id != null 
					? commonMultiLanguageNameRepository.existsByNameDefaultLangIgnoreCaseAndIdNot(name, id)
					: commonMultiLanguageNameRepository.existsByNameDefaultLangIgnoreCase(name);
		case Constants.LANGUAGE_PREFER:
			return id != null 
					? commonMultiLanguageNameRepository.existsByNamePreferLangIgnoreCaseAndIdNot(name, id)
					: commonMultiLanguageNameRepository.existsByNamePreferLangIgnoreCase(name);
		case Constants.LANGUAGE_SUPPORTIVE:
			return id != null 
					? commonMultiLanguageNameRepository.existsByNameSupportiveLangIgnoreCaseAndIdNot(name, id)
					: commonMultiLanguageNameRepository.existsByNameSupportiveLangIgnoreCase(name);
		default:
			return false; // Handle unsupported language
		}
	}

}