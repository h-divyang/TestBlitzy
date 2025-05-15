package com.catering.service.tenant.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.Constants;
import com.catering.constant.FileConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.RecordExistDto;
import com.catering.dto.tenant.request.ChangePasswordDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationDto;
import com.catering.dto.tenant.request.CompanyUserRegistrationRequestDto;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.repository.tenant.CompanyUserRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.JwtService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.CompanyUserService;
import com.catering.util.DataUtils;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CompanyUserService} interface, responsible for managing
 * company user-related operations such as user registration updates, retrieving user information,
 * and managing passwords.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @FieldDefaults: Configures field access level as private.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyUserServiceImpl implements CompanyUserService {

	/**
	 * Repository for performing database operations on CompanyUserModel.
	 */
	@Autowired
	CompanyUserRepository companyUserRepository;

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	@Autowired
	ModelMapperService modelMapperService;

	/**
	 * Utility for encrypting and verifying passwords using BCrypt.
	 */
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Service for managing and throwing custom exceptions.
	 */
	@Autowired
	ExceptionService exceptionService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	@Autowired
	MessageService messageService;

	/**
	 * Service for handling file uploads, downloads, and paths.
	 */
	@Autowired
	FileService fileService;

	/**
	 * Utility for creating, validating, and extracting JWT tokens.
	 */
	@Autowired
	JwtService jwtUtil;

	/**
	 * Service for managing company user-related functionalities.
	 */
	@Autowired
	CompanyUserService companyUserService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyUserRegistrationDto> findByUsername(String username) {
		CompanyUserModel companyUser = companyUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(""));
		if (companyUser != null) {
			companyUser.setLastLogin(LocalDateTime.now());
			companyUserRepository.save(companyUser);
		}
		return Optional.of(modelMapperService.convertEntityAndDto(companyUser, CompanyUserRegistrationDto.class));
	}

	/**
	 * Checks if the "company_user" table exists in the connected database.
	 *
	 * @param connection The database connection to use for checking the table's existence.
	 * @return {@code true} If the "company_user" table exists in the database, otherwise {@code false}.
	 * @throws SQLException If an SQL error occurs while accessing the database metadata.
	 */
	public boolean isCompanyUserTableExist(Connection connection) throws SQLException {
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet resultSet = meta.getTables(connection.getCatalog(), connection.getSchema(), Constants.COMPANY_USER_TABLE_NAME, new String[] {"TABLE"});
		return resultSet.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(CompanyUserRegistrationRequestDto companyUserDto, MultipartFile avtar) {
		boolean isExistMobile = Objects.nonNull(companyUserDto.getId()) ? companyUserRepository.existsByMobileNumberAndIdNot(companyUserDto.getMobileNumber(), companyUserDto.getId()) : companyUserRepository.existsByMobileNumber(companyUserDto.getMobileNumber());
		if (isExistMobile) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.ALREADY_EXIST), RecordExistDto.builder().isExistMobile(Boolean.TRUE).build());
		}
		if (Objects.nonNull(avtar) && Objects.nonNull(companyUserDto.getId())) {
			fileService.upload(avtar, fileService.createKey(FileConstants.MODULE_USER_PROFILE, companyUserDto.getId().toString()));
		}
		CompanyUserModel companyUserModel = modelMapperService.convertEntityAndDto(companyUserDto, CompanyUserModel.class);
		DataUtils.setAuditFields(companyUserRepository, companyUserModel.getId(), companyUserModel);
		return companyUserRepository.update(companyUserModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int changePassword(ChangePasswordDto changePasswordDto) {
		Optional<CompanyUserModel> companyUserOptional = companyUserRepository.findById(changePasswordDto.getId());
		if (companyUserOptional.isPresent()) {
			CompanyUserModel companyUserModel = companyUserOptional.get();
			boolean isMatched = bCryptPasswordEncoder.matches(changePasswordDto.getPassword(), companyUserModel.getPassword());
			if (!isMatched) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST), RecordExistDto.builder().isCurrentPasswordWrong(Boolean.TRUE).build());
			}
			companyUserModel.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword()));
			DataUtils.setAuditFields(companyUserRepository, companyUserModel.getId(), companyUserModel);
			return companyUserRepository.changePassword(companyUserModel);
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CompanyUserRegistrationDto> read(Long id) {
		if (Objects.isNull(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		Optional<CompanyUserModel> model = companyUserRepository.findById(id);
		if (model.isEmpty()) {
			exceptionService.throwRestException(HttpStatus.NOT_FOUND, messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (model.isPresent()) {
			CompanyUserRegistrationDto companyUserRegistrationDto = modelMapperService.convertEntityAndDto(model.get() , CompanyUserRegistrationDto.class);
			if (Objects.nonNull(companyUserRegistrationDto.getId())) {
				companyUserRegistrationDto.setPhoto(fileService.getUrl(FileConstants.MODULE_USER_PROFILE, companyUserRegistrationDto.getId().toString()));
			}
			return Optional.of(companyUserRegistrationDto);
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<String> resetPassword(String token, String newPassword) {
		String username = jwtUtil.extractUsername(token);
		Optional<CompanyUserRegistrationDto> companyUserDto = companyUserService.findByUsername(username);
		try {
			jwtUtil.validateToken(token, null);
		} catch (Exception e) {
			return RequestResponseUtils.generateResponseDto(messageService.getMessage(MessagesConstant.TOKEN_IS_EXIRED));
		}
		if (companyUserDto != null && companyUserDto.get().getResetPasswordToken().toString().equals(token.toString())) {
			CompanyUserModel companyUser = companyUserRepository.getById(companyUserDto.get().getId());
			companyUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
			companyUser.setResetPasswordToken(null);
			companyUserRepository.save(companyUser);
			return RequestResponseUtils.generateResponseDto(messageService.getMessage(MessagesConstant.REST_RESPONSE_RESET_PASSWORD_SUCCESSFUL));
		} else {
			return RequestResponseUtils.generateResponseDto(messageService.getMessage(MessagesConstant.REST_RESPONSE_RESET_PASSWORD_FAILED));
		}
	}

}