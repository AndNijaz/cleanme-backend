package com.cleanme.service;

import com.cleanme.dto.NotificationDto;
import com.cleanme.dto.NotificationRequest;
import com.cleanme.entity.NotificationEntity;
import com.cleanme.entity.UsersEntity;
import com.cleanme.repository.NotificationRepository;
import com.cleanme.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;

    public void createNotification(NotificationRequest request) {
        UsersEntity user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationEntity notification = new NotificationEntity();
        notification.setMessage(request.getMessage());
        notification.setUser(user);

        notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotificationsForUser(UUID userId) {
        return notificationRepository.findByUser_Uid(userId).stream()
                .map(n -> new NotificationDto(
                        n.getId(),
                        n.getMessage(),
                        n.isRead(),
                        n.getCreatedAt()
                ))
                .toList();
    }

    public void markNotificationAsRead(UUID id) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}