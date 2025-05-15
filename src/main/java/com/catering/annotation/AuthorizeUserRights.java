package com.catering.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation used to authorize user rights for method execution.
 * <p>
 * This annotation is applied to methods to ensure that the user has the required rights before allowing access to the method.
 * It checks if the user has one or more specified rights, which are typically defined within the application and tied to user roles.
 * The rights are specified as a list of strings in the {@link #value()} field. The method can be accessed only if the current user has 
 * all or at least one of the specified rights, depending on the {@link #checkAll()} setting.
 * </p>
 * <p>
 * The {@link #checkAll()} field allows for flexibility in the authorization process:
 * <ul>
 * <li>If {@code checkAll = true}, the method will require all user rights listed in {@link #value()} to be granted.</li>
 * <li>If {@code checkAll = false}, the method will require only one of the user rights listed in {@link #value()} to be granted.</li>
 * </ul>
 * </p>
 * <p>
 * This annotation should be used on methods in controllers, service methods, or other business logic methods that require user authorization.
 * The user rights are typically defined as constants (e.g., {@link ApiUserRightsConstants}) and are mapped to roles or permissions in the system.
 * </p>
 * 
 * @author Krushali Talaviya
 * @since 2024-05-07
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeUserRights {

	/**
	 * Array of user rights to check for the current method.
	 * These rights are expected to be defined in the application and linked to specific roles.
	 * 
	 * @return An array of user rights.
	 */
	String[] value();

	/**
	 * Indicates whether all the provided user rights should be checked for access.
	 * If true, all rights in the value array must be satisfied. If false, only one right needs to be present.
	 * Default value is true, meaning all rights must be checked.
	 * 
	 * @return boolean indicating if all user rights should be checked.
	 */
	boolean checkAll() default true;

}