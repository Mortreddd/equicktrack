package com.it43.equicktrack.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CrudRepository<Notification, Long> {
}
