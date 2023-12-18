package org.weebook.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.weebook.api.dto.NotificationDto;
import org.weebook.api.entity.Notification;
import org.weebook.api.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {Instant.class, List.class, ArrayList.class})
public interface NotificationMapper {
    @Mapping(target = "isRead", expression = "java(false)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "users", ignore = true)
    Notification notification(String title, String message, String type, User user);

    @Mapping(target = "isRead", expression = "java(false)")

    Notification notification(String title, String message, String type);

    NotificationDto entityToDto(Notification notification);

    List<NotificationDto> entityToDtos(List<Notification> notification);

}
