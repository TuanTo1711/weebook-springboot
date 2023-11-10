package org.weebook.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.weebook.api.dto.NotificationDto;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        imports = {UUID.class, Instant.class, List.class, ArrayList.class, LinkedHashSet.class})
public interface NotificationMapper {
    @Mapping(target = "isRead", expression = "java(false)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Notification notification(String title, String message, String type, User user);

    NotificationDto entityToDto(Notification notification);

    List<NotificationDto> entityToDtos(List<Notification> notification);

}
