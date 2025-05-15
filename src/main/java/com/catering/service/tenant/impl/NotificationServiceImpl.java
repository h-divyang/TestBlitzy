package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.NotificationDto;
import com.catering.model.tenant.CompanyUserModelForAudit;
import com.catering.repository.tenant.NotificationRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.NotificationService;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the NotificationService interface that provides functionality
 * for managing notifications, including retrieving a list of notifications and
 * marking them as read.
 *
 * This class integrates with the NotificationRepository to access and modify
 * notification data stored in the database. It also leverages the ModelMapperService
 * to convert entities to DTOs for data transfer purposes.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

	/**
	 * Repository for managing notifications.
	 */
	NotificationRepository notificationRepository;

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<NotificationDto> read() {
		return modelMapperService.convertListEntityAndListDto(notificationRepository.findAllByOrderByCreatedAtDesc(), NotificationDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void markAsRead(Long notificationId) {
		Optional<CompanyUserModelForAudit> auditor = DataUtils.getCurrentAuditor();
		if (notificationId == 0) {
			notificationRepository.markAllAsRead(auditor.get().getId());
		} else {
			notificationRepository.markAsRead(notificationId, auditor.get().getId());
		}
	}

}