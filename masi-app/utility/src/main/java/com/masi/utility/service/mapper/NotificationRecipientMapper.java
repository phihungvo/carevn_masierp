package com.masi.utility.service.mapper;

import com.masi.utility.domain.Notification;
import com.masi.utility.domain.NotificationRecipient;
import com.masi.utility.service.dto.NotificationDTO;
import com.masi.utility.service.dto.NotificationRecipientDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationRecipient} and its DTO {@link NotificationRecipientDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationRecipientMapper extends EntityMapper<NotificationRecipientDTO, NotificationRecipient> {
    @Mapping(target = "notification", source = "notification", qualifiedByName = "notificationId")
    NotificationRecipientDTO toDto(NotificationRecipient s);

    @Named("notificationId")
    NotificationDTO toDtoNotificationId(Notification notification);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
