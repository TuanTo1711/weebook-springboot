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
import org.weebook.api.web.request.PagingRequest;

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

    //đếm số transaction chưa đọc
    @GetMapping("/get/notification/total")
    public Long getAllNotification(){
        return userService.getAllNotificationTotal();
    }

    @PutMapping("/update/notification/{idNotification}")
    public void updateNotification(@PathVariable("idNotification") Long idNotification){
        userService.updateNotification(idNotification);
    }
}
