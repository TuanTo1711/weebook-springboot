package org.weebook.api.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.NotificationDto;
import org.weebook.api.dto.TransactionDto;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.UserMapper;
import org.weebook.api.entity.User;
import org.weebook.api.service.UserService;
import org.weebook.api.web.request.DogRequest;
import org.weebook.api.web.request.PagingRequest;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserDetailsManager userDetailsService;
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("get-by-username")
    public UserDto getUserByUsername(@RequestParam @NotNull @NotEmpty @NotBlank String username) {
        User userDetails = (User) userDetailsService.loadUserByUsername(username);
        return userMapper.toDto(userDetails);
    }

    @PostMapping("/get/transaction")
    public List<TransactionDto> getAllTransaction(@RequestBody PagingRequest pagingRequest){
        return userService.getAllTransaction(pagingRequest);
    }

    @PostMapping("/get/notification")
    public List<NotificationDto> getAllNotification(@RequestBody PagingRequest pagingRequest){
        return userService.getAllNotification(pagingRequest);
    }

    @GetMapping("/get/notification/total")
    public Long getAllNotification(){
        return userService.getAllNotificationUnread();
    }

    @PutMapping("/update/notification/{id}")
    public void updateNotification(@PathVariable("id") Long idNotification){
        userService.updateNotification(idNotification);
    }

    @GetMapping("/dog")
    public List<UserDto> dog(LocalDate dateMin, LocalDate dateMax, Integer max, PagingRequest pagingRequest){
        return userService.getDog(dateMin,dateMax,max, pagingRequest);
    }



}
