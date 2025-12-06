package com.sufaltalukder.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.NotificationModel;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

	@Query(value = "SELECT * FROM notification_tbl WHERE notification_id = :notificationId", nativeQuery = true)
	NotificationModel findByNotificationId(@Param("notificationId") long notificationId);

	@Query(value = "SELECT * FROM notification_tbl WHERE user_id = :userId ORDER BY created_at DESC", nativeQuery = true)
	List<NotificationModel> findAllNotificationsByUserId(@Param("userId") long userId);

}
