package com.catering.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.commons.lang3.StringUtils;

import com.catering.constant.MessagesConstant;
import com.catering.util.ValidationUtils;

/**
 * Custom annotation for validating email addresses.
 * <p>
 * This annotation is used to validate whether a given string is a valid email address. The validation is performed using a custom validator, {@link EmailValidator}.
 * It is typically applied to fields in DTOs or entities where email format validation is required.
 * </p>
 * <p>
 * The {@link #message()} field allows for a customizable error message when the validation fails. By default, the message is defined in {@link MessagesConstant#VALIDATION_EMAIL_NOT_VALID}.
 * The {@link #isMandatory()} field defines whether the email field is mandatory. If set to {@code false}, the field can be blank and will not trigger validation. The default value is {@code true}.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {

	/**
	 * The error message to be shown when the email validation fails.
	 * 
	 * @return The error message string.
	 */
	String message() default MessagesConstant.VALIDATION_EMAIL_NOT_VALID;

	/**
	 * Flag indicating whether the email field is mandatory.
	 * If {@code true}, the field must contain a valid email. If {@code false}, the field can be left blank.
	 * 
	 * @return {@code true} if the field is mandatory, {@code false} if it can be blank.
	 */
	boolean isMandatory() default true;

	/**
	 * Validation groups to assign to the annotation.
	 * 
	 * @return The groups for validation.
	 */
	Class<?>[] groups() default { };

	/**
	 * Additional data to carry during validation.
	 * 
	 * @return The payload data for the validation.
	 */
	Class<? extends Payload>[] payload() default { };

}

/**
 * Validator for the {@link Email} annotation.
 * <p>
 * This validator checks if a given string is a valid email address. The email validation is performed using a utility class, {@link ValidationUtils}.
 * The validator respects the {@link Email#isMandatory()} setting:
 * - If {@code isMandatory} is {@code true}, the email must be a valid email format.
 * - If {@code isMandatory} is {@code false}, the email field can be blank or null without triggering validation.
 * </p>
 * 
 * @see ValidationUtils
 */
class EmailValidator implements ConstraintValidator<Email, String> {

	private boolean isMandatory;

	/**
	 * Initializes the validator with the {@link Email} annotation's values.
	 * 
	 * @param constraintAnnotation The {@link Email} annotation being validated.
	 */
	@Override
	public void initialize(Email constraintAnnotation) {
		isMandatory = constraintAnnotation.isMandatory();
	}

	/**
	 * Validates the email value.
	 * <p>
	 * If the email is not mandatory and is blank, validation passes.
	 * Otherwise, the email must follow the standard email format as validated by {@link ValidationUtils#isEmail(String)}.
	 * </p>
	 * 
	 * @param value The value to validate.
	 * @param context The context in which the validation is being performed.
	 * @return {@code true} if the value is a valid email or blank if not mandatory, {@code false} otherwise.
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (!isMandatory && StringUtils.isBlank(value)) {
			return true;
		}
		return ValidationUtils.isEmail(value);
	}

}