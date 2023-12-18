package org.weebook.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.weebook.api.dto.NotificationDto;
import org.weebook.api.dto.TransactionDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.web.request.PagingRequest;

import java.time.LocalDate;
import java.util.List;

public interface UserService extends UserDetailsManager {
    UserDetails loadUserByEmail(String email);

    List<TransactionDto> getAllTransaction(PagingRequest pagingRequest);

    List<NotificationDto> getAllNotification(PagingRequest pagingRequest);

    Long getAllNotificationUnread();

    void updateNotification(Long id);
}
