package com.homework.social_network.service.notification;

import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.notification.NotificationFullResponseDto;
import com.homework.social_network.dto.response.notification.NotificationResponseDto;
import com.homework.social_network.dto.response.notification.NotificationsResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Notification;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.enums.NotificationType;
import com.homework.social_network.repo.NotificationRepository;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.utils.CustomPageable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;
import static com.homework.social_network.utils.Utils.getCurrentDateTime;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public NotificationsResponseDto getUserNotifications(UUID targetId, LocalDateTime startLoadTime, int start, int end) {
        Pageable pageable = new CustomPageable(start, end - start, Sort.by(Sort.Direction.DESC, "createDate"));
        List<Notification> result = notificationRepository.getNotifications(targetId, startLoadTime, pageable);
        Map<UUID, Account> accounts = accountService.getBatch(new ArrayList<>(result.stream()
                .map(Notification::getSourceId)
                .collect(Collectors.toSet())));
        List<NotificationFullResponseDto> notifications = result.stream()
                .map(m -> {
                    NotificationFullResponseDto dto = modelMapper.map(m, NotificationFullResponseDto.class);
                    Account account = accounts.get(m.getSourceId());
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    dto.setSource(simple);
                    return dto;
                })
                .collect(Collectors.toList());
        return NotificationsResponseDto.builder()
                .notifications(notifications)
                .startLoadTime(startLoadTime)
                .totalSize(notificationRepository.getNotificationsCount(targetId, startLoadTime))
                .build();
    }

    @Transactional
    public void acceptNotifications(List<UUID> notificationIds) {
        notificationRepository.acceptNotifications(notificationIds);
    }

    @Transactional
    public List<NotificationResponseDto> getUnacceptedNotifications() {
        UUID currentAccountId = getCurrentAccountId();
        return notificationRepository.getByDestinationIdAndAccepted(currentAccountId, false)
                .stream()
                .map(m -> modelMapper.map(m, NotificationResponseDto.class))
                .collect(Collectors.toList());
    }

    public void createNotificationAddPartner(UUID sourceId, UUID oldPartner, UUID newPartner) {
        if (Objects.equals(newPartner, oldPartner)) {
            return;
        }

        if (Objects.nonNull(oldPartner)) {
            createNotificationInternal(sourceId, oldPartner, NotificationType.REMOVE_PARTNER);
        }
        if (Objects.nonNull(newPartner)) {
            createNotificationInternal(sourceId, oldPartner, NotificationType.ADD_PARTNER);
        }
    }

    public void createNotificationAddFriend(UUID sourceId, UUID destinationId, FriendRelationStatus resultStatus) {
        NotificationType notificationType = resultStatus == FriendRelationStatus.FULL ? NotificationType.ACCEPT_FRIEND : NotificationType.ADD_FRIEND;
        createNotificationInternal(sourceId, destinationId, notificationType);
    }

    public void createNotificationRemoveFriend(UUID sourceId, UUID destinationId, FriendRelationStatus resultStatus) {
        NotificationType notificationType = resultStatus == FriendRelationStatus.NO_RELATION ? NotificationType.CANCEL_FRIEND : NotificationType.REMOVE_FRIEND;
        createNotificationInternal(sourceId, destinationId, notificationType);
    }

    public void createNotificationRejectFriend(UUID sourceId, UUID destinationId) {
        NotificationType notificationType = NotificationType.REJECT_FRIEND;
        createNotificationInternal(sourceId, destinationId, notificationType);
    }

    public void createNotificationInternal(UUID sourceId, UUID destinationId, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setSourceId(sourceId);
        notification.setDestinationId(destinationId);
        notification.setNotificationType(notificationType);
        notification.setAccepted(false);
        notification.setCreateDate(getCurrentDateTime());
        NotificationResponseDto responseDto = modelMapper.map(notificationRepository.save(notification), NotificationResponseDto.class);
        simpMessagingTemplate.convertAndSend("/queue/notification/" + responseDto.getDestinationId(), responseDto);
    }
}
