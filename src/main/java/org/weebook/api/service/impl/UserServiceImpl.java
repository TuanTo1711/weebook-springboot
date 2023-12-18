package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.weebook.api.dto.NotificationDto;
import org.weebook.api.dto.TransactionDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.OrderMapper;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.Transaction;
import org.weebook.api.entity.User;
import org.weebook.api.repository.NotificationRepository;
import org.weebook.api.repository.UserRepository;
import org.weebook.api.service.UserService;
import org.weebook.api.web.request.PagingRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.weebook.api.exception.error.ErrorMessages.ACCOUNT_NOT_FOUND_ERROR;

@Service("userDetailsService")
@RequiredArgsConstructor
@Slf4j
@Setter
public class UserServiceImpl implements UserService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationMapper notificationMapper;
    private final OrderMapper orderMapper;
    private SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public UserDetails loadUserByUsername(String username) {
        Assert.notNull(username, "Username must be not null");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_FOUND_ERROR));
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        Assert.notNull(email, "Email must be not null");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_FOUND_ERROR));
    }

    @Override
    public void createUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Assert.isTrue(!this.userExists(username), "User with username: %s exists".formatted(username));
        User user = (User) userDetails;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = (User) userDetails;
        Assert.isTrue(this.userExists(user.getUsername()), "User not exits");
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        User user = getUser();
        Assert.state(user != null, "Current user doesn't exist in database.");
        Assert.state(passwordEncoder.matches(oldPassword, user.getPassword()), "Old password don't match.");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<TransactionDto> getAllTransaction(PagingRequest pagingRequest) {
        User user = getUser();
        var pageable = PageRequest.of(pagingRequest.getPageNumber() - 1, pagingRequest.getPageSize());
        Page<Transaction> transactions = userRepository.getAllTransaction(user.getId(), pageable);
        return orderMapper.entityTransactionToDtos(transactions.getContent());
    }

    @Override
    public List<NotificationDto> getAllNotification(PagingRequest pagingRequest) {
        User user = getUser();
        System.out.println(user.getId());
        var pageable = PageRequest.of(pagingRequest.getPageNumber() - 1, pagingRequest.getPageSize());
        Page<Notification> notification = userRepository.getAllNotification(user.getId(), pageable);
        return notificationMapper.entityToDtos(notification.getContent());
    }

    @Override
    public Long getAllNotificationUnread() {
        User user = getUser();
        return userRepository.getAllNotificationTotal(user.getId());
    }

    public void updateNotification(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isEmpty()) {
            return;
        }
        Notification notification = optionalNotification.get();
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    private User getUser() {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        return (User) loadUserByUsername(currentUser.getName());
    }
}
