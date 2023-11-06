package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepo;

import java.nio.file.AccessDeniedException;

import static org.weebook.api.exception.ErrorMessages.ACCOUNT_NOT_FOUND_ERROR;

@Service("UserDetailsService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsManager {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_FOUND_ERROR));
    }


    @Override
    public void createUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Assert.isTrue(!this.userExists(username),"User with username: % exits".formatted(username));
        User user = (User) userDetails;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Assert.isTrue(this.userExists(username), "User not exits");
        User user = (User) userDetails;
        userRepo.save(user);

    }

    @Override
    public void deleteUser(String username) {
        userRepo.deleteByUsername(username);
    }

    @SneakyThrows
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = securityContextHolderStrategy.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)){
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        }else{
            String username = currentUser.getName();
            User user = (User) loadUserByUsername(username);
            Assert.state(user != null, "Current User is not exits database.");
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.saveAndFlush(user);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepo.existsByUsername(username);
    }
}
