package org.weebook.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.weebook.api.dto.NotificationDto;
import org.weebook.api.dto.TransactionDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.OrderMapper;
import org.weebook.api.dto.mapper.UserMapper;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.Transaction;
import org.weebook.api.entity.User;
import org.weebook.api.repository.NotificationRepository;
import org.weebook.api.repository.UserRepository;
import org.weebook.api.web.request.DogRequest;
import org.weebook.api.web.request.PagingRequest;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final OrderMapper orderMapper;
    final NotificationMapper notificationMapper;
    final NotificationRepository notificationRepository;
    final UserMapper userMapper;

    private final SecurityContextHolderStrategy securityContextHolder
            = SecurityContextHolder.getContextHolderStrategy();
    private final UserDetailsService userDetailsService;
    public List<TransactionDto> getAllTransaction(PagingRequest pagingRequest){
        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        Pageable pageable = PageRequest.of(pagingRequest.getPageNumber()-1, pagingRequest.getPageSize());
        Page<Transaction> transactions = userRepository.getAllTransaction(user.getId(), pageable);
        return orderMapper.entityTransactionToDtos(transactions.getContent());
    }

    public List<NotificationDto> getAllNotification(PagingRequest pagingRequest){
        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        Pageable pageable = PageRequest.of(pagingRequest.getPageNumber()-1, pagingRequest.getPageSize());
        Page<Notification> notification = userRepository.getAllNotification(user.getId(), pageable);
        return notificationMapper.entityToDtos(notification.getContent());
    }

    public Long getAllNotificationTotal(){
        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        return userRepository.getAllNotificationTotal(user.getId());
    }

    public void updateNotification(Long id){
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if(optionalNotification.isEmpty()){
            return;
        }
        Notification notification = optionalNotification.get();
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public List<UserDto> getDog(DogRequest dogRequest){
        Pageable pageable = PageRequest.of(dogRequest.getPagingRequest().getPageNumber(), dogRequest.getPagingRequest().getPageSize());
        List<User> users = userRepository.get강아지(dogRequest.getDateMin(), dogRequest.getDateMax(), dogRequest.getMax(), pageable);
        return userMapper.toDtos(users);
    }
}
