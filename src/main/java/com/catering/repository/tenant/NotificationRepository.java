package com.catering.repository.tenant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.NotificationModel;

/**
 * Repository interface for managing {@link NotificationModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations and custom query methods 
 * for managing notifications in the application.
 * 
 * Custom methods:
 * - {@link #markAsRead(Long, Long)}: Marks a specific notification as read, updating the read timestamp and the user who read it.
 * - {@link #markAllAsRead(Long)}: Marks all unread notifications as read for a given user.
 * - {@link #findAllByOrderByCreatedAtDesc()}: Retrieves all notifications ordered by their creation date in descending order.
 */
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

	/**
	 * Marks a specific notification as read.
	 * 
	 * @param notificationId the ID of the notification to be marked as read.
	 * @param readBy the ID of the user who read the notification.
	 */
	@Modifying
	@Transactional
	@Query("UPDATE NotificationModel n SET n.isRead = true, n.readAt = NOW(), n.readBy = :readBy WHERE n.id = :notificationId")
	void markAsRead(@Param(value = "notificationId") Long notificationId, @Param(value = "readBy") Long readBy);

	/**
	 * Marks all unread notifications as read.
	 * 
	 * @param readBy the ID of the user who read the notifications.
	 */
	@Modifying
	@Transactional
	@Query("UPDATE NotificationModel n SET n.isRead = true, n.readAt = NOW(), n.readBy = :readBy WHERE n.isRead = false")
	void markAllAsRead(@Param(value = "readBy") Long readBy);

	/**
	 * Retrieves all notifications ordered by their creation date in descending order.
	 * 
	 * @return a list of notifications ordered by creation date (most recent first).
	 */
	List<NotificationModel> findAllByOrderByCreatedAtDesc();

}