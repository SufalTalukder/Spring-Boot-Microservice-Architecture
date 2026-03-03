package com.sufaltalukder.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sufaltalukder.Models.NotificationModel;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

	@Query("""
				SELECT nm
				 FROM NotificationModel nm
				WHERE nm.notificationId = :notificationId
			""")
	Optional<NotificationModel> findByNotificationId(@Param("notificationId") long notificationId);

	@Query(value = "SELECT * FROM notification_tbl WHERE user_id = :userId ORDER BY created_at DESC", nativeQuery = true)
	List<NotificationModel> findAllNotificationsByUserId(@Param("userId") long userId);

	@Query("""
				SELECT nm
				 FROM NotificationModel nm
				ORDER BY nm.notificationCreatedAt ASC
			""")
	List<NotificationModel> findAllNotifications();

}
