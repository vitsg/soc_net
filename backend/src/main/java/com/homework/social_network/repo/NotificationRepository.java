package com.homework.social_network.repo;

import com.homework.social_network.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, UUID> {

    @Query(value = "select n from Notification n where n.destinationId = :targetId and n.createDate <= :startLoadTime")
    List<Notification> getNotifications(UUID targetId, LocalDateTime startLoadTime, Pageable pageable);

    @Query(value = "select count(n) from Notification n where n.destinationId = :targetId and n.createDate <= :startLoadTime")
    long getNotificationsCount(UUID targetId, LocalDateTime startLoadTime);

    @Modifying
    @Query("update Notification n set n.accepted = true where n.id in :notificationIds")
    void acceptNotifications(List<UUID> notificationIds);

    List<Notification> getByDestinationIdAndAccepted(UUID destinationId, boolean accepted);
}
