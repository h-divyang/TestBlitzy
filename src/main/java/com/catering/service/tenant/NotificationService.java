package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.NotificationDto;

/**
 * Service interface for managing notifications.
 * This interface provides methods to interact with notification-related functionalities,
 * including retrieving a list of notifications and marking notifications as read.
 *
 * Implementations of this interface are responsible for integrating with the data access
 * layer and performing these operations while adhering to the defined interface methods.
 */
public interface NotificationService {

	/**
	 * Retrieves a list of notifications.
	 *
	 * @return A list of NotificationDto objects representing the notifications.
	 */
	List<NotificationDto> read();

	/**
	 * Marks a specific notification as read.
	 *
	 * @param notificationId The unique identifier of the notification to be marked as read.
	 */
	void markAsRead(Long notificationId);

}