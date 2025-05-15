package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.Constants;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.NotificationDto;
import com.catering.service.tenant.NotificationService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This controller manages the notifications for users.
 * It provides endpoints to retrieve notifications and mark them as read.
 * The controller is mapped under the API path defined in `ApiPathConstant.NOTIFICATION`.
 */
@RestController
@RequestMapping(value = ApiPathConstant.NOTIFICATION)
@Tag(name = SwaggerConstant.NOTIFICATION)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {

	/**
	 * The service used to handle the business logic related to notifications.
	 * This service is injected into the controller via constructor injection.
	 */
	NotificationService notificationService;

	/**
	 * Endpoint to retrieve a list of notifications.
	 * It fetches notifications that have not been marked as read, or all notifications 
	 * depending on the logic in the `NotificationService.read()` method.
	 * 
	 * @return a `ResponseContainerDto` containing a list of `NotificationDto` objects.
	 * The response container ensures a consistent structure for the API response.
	 * 
	 * @apiNote This method is accessible via HTTP GET requests to the base path defined in `ApiPathConstant.NOTIFICATION`.
	 * 
	 */
	@GetMapping
	public ResponseContainerDto<List<NotificationDto>> read() {
		return RequestResponseUtils.generateResponseDto(notificationService.read());
	}

	/**
	 * Endpoint to mark a specific notification as read.
	 * This method accepts a notification ID as a request parameter, and updates
	 * the notification's status in the database to indicate it has been read.
	 * 
	 * @param notificationId the ID of the notification that should be marked as read.
	 * This ID is provided as a request parameter.
	 * 
	 * @apiNote This method is accessible via HTTP GET requests to the path defined in
	 * `ApiPathConstant.MARK_AS_READ`. The request should include a valid notification ID.
	 * 
	 */
	@GetMapping(value = ApiPathConstant.MARK_AS_READ)
	public void markAsRead(@RequestParam(value = Constants.NOTIFICATION_ID) Long notificationId) {
		notificationService.markAsRead(notificationId);
	}

}