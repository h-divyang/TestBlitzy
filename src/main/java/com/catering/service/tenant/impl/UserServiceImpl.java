package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.bean.ErrorGenerator;
import com.catering.constant.FieldConstants;
import com.catering.constant.FileConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.common.RoleDto;
import com.catering.dto.tenant.request.UserDto;
import com.catering.dto.tenant.request.UserRequestDto;
import com.catering.enums.RoleEnum;
import com.catering.exception.RestException;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.repository.tenant.CompanyUserRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.S3Service;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.UserRightsService;
import com.catering.service.tenant.UserService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation class for the UserService interface.
 * This class provides the concrete implementation of methods for managing user-related operations.
 * It extends the GenericServiceImpl class with 'UserDto', 'CompanyUserModel', and 'Long' as generic types.
 * The 'UserServiceImpl' is responsible for handling user-related business logic and coordinating
 * with the data access layer through the 'CompanyUserRepository'.
 *
 * This service class provides functionality for creating, reading, and updating user data, as well as
 * retrieving roles and active users. It also implements the 'getExampleMatcher()' method to obtain an
 * 'ExampleMatcher' for performing a case-insensitive partial matching on user data fields in a
 * Query by Example (QBE) search.
 *
 * Note: This class is intended to be a service implementation for user-related operations. It uses
 *		 the 'CompanyUserRepository' to interact with the underlying database. The 'ModelMapperService'
 *		 is used for mapping between DTOs and entities. The 'MessageService' is used for retrieving
 *		 localized messages. The 'ExceptionService' is used for throwing custom exceptions in case of
 *		 validation errors or bad requests.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @see UserService
 * @see GenericServiceImpl
 * @see BCryptPasswordEncoder
 * @see ExampleMatcher
 * @see RoleDto
 * @see UserRequestDto
 * @see ResponseContainerDto
 * @see ValidationUtils
 * @see StringUtils
 * @author krushali talaviya
 * @since July 2023
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl extends GenericServiceImpl<UserDto, CompanyUserModel, Long> implements UserService {

	/**
	 * Repository for managing company user information.
	 */
	CompanyUserRepository companyUserRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Encoder for encrypting passwords using the BCrypt algorithm.
	 */
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Service for managing user rights and permissions.
	 */
	UserRightsService userRightsService;

	/**
	 * Service for handling file-related operations.
	 */
	FileService fileService;

	/**
	 * Service for interacting with Amazon S3 storage.
	 */
	S3Service s3Service;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<UserRequestDto> create(UserRequestDto userRequestDto, MultipartFile img) {
		validateFields(userRequestDto);
		userRequestDto.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
		CompanyUserModel companyUserModel = modelMapperService.convertEntityAndDto(userRequestDto, CompanyUserModel.class);
		DataUtils.setAuditFields(companyUserRepository, userRequestDto.getId(), companyUserModel);
		UserDto userDto = modelMapperService.convertEntityAndDto(companyUserRepository.save(companyUserModel), UserDto.class);
		if (Objects.nonNull(img)) {
			fileService.upload(img, fileService.createKey(FileConstants.MODULE_USER_PROFILE, userDto.getId().toString()));
		}
		// Set the user right data
		userRightsService.setUserDataWithMainMenuAndSubMenu(companyUserModel, false);
		return Optional.of(modelMapperService.convertEntityAndDto(companyUserModel, UserRequestDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto update(UserDto userDto, MultipartFile img) {
		validateFields(userDto);
		if (Boolean.FALSE.equals(userDto.getIsActive()) && companyUserRepository.existsByReportsTo(userDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		CompanyUserModel companyUserModel = modelMapperService.convertEntityAndDto(userDto, CompanyUserModel.class);
		DataUtils.setAuditFields(companyUserRepository, userDto.getId(), companyUserModel);
		int affectedRows = companyUserRepository.updateUser(companyUserModel, companyUserModel.getUpdatedBy());
		if (affectedRows != 1) {
			exceptionService.throwInternalServerErrorRestException();
		}
		if (Objects.nonNull(img)) {
			fileService.upload(img, fileService.createKey(FileConstants.MODULE_USER_PROFILE, userDto.getId().toString()));
		}
		return modelMapperService.convertEntityAndDto(companyUserModel, UserDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<UserDto>> read(FilterDto filterDto, HttpServletRequest request) {
		Optional<Example<CompanyUserModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			CompanyUserModel companyUserModel = CompanyUserModel.ofSearchingModel(query);
			companyUserModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(companyUserModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<UserDto>> userDto = read(UserDto.class, CompanyUserModel.class, filterDto, example);
		userDto.getBody().forEach(user -> 
			user.setPhoto(fileService.createStaticUrl(FileConstants.MODULE_USER_PROFILE, user.getId().toString()))
		);
		return userDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<RoleDto>> read() {
		return RequestResponseUtils.generateResponseDto(RoleEnum.ROLES);
	}

	/**
	 * {@inheritDoc	}
	 */
	@Override
	public List<UserDto> readUserByIsActive() {
		List<CompanyUserModel> companyUserModel = companyUserRepository.findAllByIsActiveOrderByIdAsc(true);
		return modelMapperService.convertListEntityAndListDto(companyUserModel, UserDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id, Boolean isImage) {
		s3Service.delete(fileService.createKey(FileConstants.MODULE_USER_PROFILE, id.toString()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<UserDto> updateStatus(Long id, Boolean status) throws RestException {
		if (Boolean.FALSE.equals(status) && companyUserRepository.existsByReportsTo(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		Optional<CompanyUserModel> companyUserModel = companyUserRepository.findById(id);
		if (companyUserModel.isPresent()) {
			companyUserModel.get().setIsActive(status);
		}
		return Optional.of(modelMapperService.convertEntityAndDto(companyUserRepository.save(companyUserModel.get()), UserDto.class));
	}

	/**
	 * Get an ExampleMatcher for building criteria to match objects in a Query by Example (QBE) search.
	 *
	 * This method configures the ExampleMatcher to perform a case-insensitive partial matching
	 * (contains) on various fields relevant to user data. 
	 *
	 * The method also excludes audit fields from consideration during the matching process.
	 * The 'ExampleMatcher' is used to define how fields should be matched when constructing
	 * queries based on the example entity. In this case, it uses the 'matchingAny()' option
	 * to match the entity by any of the specified fields.
	 *
	 * @return An 'ExampleMatcher' configured for partial case-insensitive matching on user data fields.
	 * @see ExampleMatcher
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.USER_FIRST_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.USER_FIRST_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.USER_FIRST_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.USER_LAST_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.USER_LAST_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.USER_LAST_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.USER_USERNAME, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsName()));
	}

	/**
	 * Validates the fields in the provided 'userDto' object.
	 *
	 * This method performs various validation checks on the fields of the 'userDto' object to ensure data integrity
	 * and to avoid duplicate entries. The method checks for existing usernames, emails, and mobile numbers in the
	 * database using the 'companyUserRepository'. If any of these fields are found to be duplicate for an existing user,
	 * corresponding validation errors are added to the 'ErrorGenerator'.
	 *
	 * Additionally, the method checks if the 'designationId' in the 'userDto' matches any valid role ID from the 'RoleEnum'.
	 * If the 'designationId' is invalid, a validation error is added to the 'ErrorGenerator'.
	 *
	 * Furthermore, the method checks if the 'reportsTo' field refers to an existing and active user in the database. If
	 * the 'reportsTo' user is not found or is inactive, a validation error is added to the 'ErrorGenerator'.
	 *
	 * If any validation errors are present in the 'ErrorGenerator', a bad request exception is thrown using the
	 * 'exceptionService', and the error messages are taken from the 'messageService'.
	 */
	private void validateFields(UserDto userDto) {
		ErrorGenerator error = ErrorGenerator.builder();
		if ((Objects.nonNull(userDto.getId()) && companyUserRepository.existsByUsernameAndIdNot(userDto.getUsername(), userDto.getId())) || (Objects.isNull(userDto.getId()) && companyUserRepository.existsByUsername(userDto.getUsername()))) {
			error.putError(FieldConstants.USER_USERNAME, MessagesFieldConstants.USER_FIELD_USERNAME + messageService.getMessage(MessagesConstant.VALIDATION_USER_FIELD_ALREADY_EXIST));
		}
		if ((Objects.nonNull(userDto.getId()) && Objects.nonNull(userDto.getEmail()) && companyUserRepository.existsByEmailAndIdNot(userDto.getEmail(), userDto.getId())) || (Objects.isNull(userDto.getId()) && Objects.nonNull(userDto.getEmail()) && companyUserRepository.existsByEmail(userDto.getEmail()))) {
			error.putError(FieldConstants.COMMON_FIELD_EMAIL, MessagesFieldConstants.COMMON_FIELD_EMAIL + messageService.getMessage(MessagesConstant.VALIDATION_USER_FIELD_ALREADY_EXIST));
		}
		if ((Objects.nonNull(userDto.getId()) && Objects.nonNull(userDto.getMobileNumber()) && companyUserRepository.existsByMobileNumberAndIdNot(userDto.getMobileNumber(), userDto.getId())) || (Objects.isNull(userDto.getId()) && Objects.nonNull(userDto.getMobileNumber()) && companyUserRepository.existsByMobileNumber(userDto.getMobileNumber()))) {
			error.putError(FieldConstants.CONTACT_FIELD_MOBILE_NUMBER, MessagesFieldConstants.COMMON_FIELD_MOBILE_NUMBER + messageService.getMessage(MessagesConstant.VALIDATION_USER_FIELD_ALREADY_EXIST));
		}
		if (Objects.nonNull(userDto.getDesignationId()) && RoleEnum.ROLES.stream().noneMatch(role -> role.getId().equals(userDto.getDesignationId()))) {
			error.putError(FieldConstants.USER_DESIGNATION_ID, MessagesFieldConstants.USER_FIELD_DESIGNATION_ID + messageService.getMessage(MessagesConstant.VALIDATION_IS_INVALID));
		}
		if (Objects.nonNull(userDto.getReportsTo()) && !companyUserRepository.existsByIdAndIsActive(userDto.getReportsTo(), true)) {
			error.putError(FieldConstants.USER_REPORTS_TO, MessagesFieldConstants.USER_REPORTS_TO + messageService.getMessage(MessagesConstant.VALIDATION_IS_INVALID));
		}
		if (error.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), error.getErrors());
		}
	}

}