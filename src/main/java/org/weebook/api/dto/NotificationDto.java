package org.weebook.api.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.weebook.api.entity.User;

import java.time.Instant;

@Getter
@Setter
public class NotificationDto {
    private Long id;

    private String title;

    private String message;


    private Boolean isRead;

    private String type;


    private Instant createdDate;

}
