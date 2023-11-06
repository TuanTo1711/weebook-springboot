package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
