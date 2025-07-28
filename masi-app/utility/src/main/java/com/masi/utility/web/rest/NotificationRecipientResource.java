package com.masi.utility.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.utility.repository.NotificationRecipientRepository;
import com.masi.utility.service.NotificationRecipientService;
import com.masi.utility.service.dto.NotificationRecipientDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * REST controller for managing {@link com.masi.utility.domain.NotificationRecipient}.
 */
@RestController
@RequestMapping("/api/notification-recipients")
public class NotificationRecipientResource {

    private static final Logger log = LoggerFactory.getLogger(NotificationRecipientResource.class);

    private static final String ENTITY_NAME = "masiUtilityNotificationRecipient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationRecipientService notificationRecipientService;

    private final NotificationRecipientRepository notificationRecipientRepository;

    public NotificationRecipientResource(
        NotificationRecipientService notificationRecipientService,
        NotificationRecipientRepository notificationRecipientRepository
    ) {
        this.notificationRecipientService = notificationRecipientService;
        this.notificationRecipientRepository = notificationRecipientRepository;
    }


    @GetMapping("my-notifications")
    public Mono<ResponseEntity<ApiResponse<NotificationRecipientDTO>>> getMyNotifications(@ParameterObject Pageable pageable) {
        log.debug("REST request to get all NotificationRecipients by recipientId");
        return notificationRecipientService.getMyNotifications(pageable)
            .collectList()
            .zipWith(notificationRecipientService.countMyNotifications())
            .map(list -> new ApiResponse<>(list.getT1(), list.getT2()))
            .map(response -> ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(response.getTotalRecord()))
                .body(response)
            );
    }

    @PatchMapping("{id}/read")
    public Mono<ResponseEntity<Object>> markAsRead(@PathVariable UUID id) {
        return notificationRecipientService.markAsRead(List.of(id))
            .then(Mono.just(ResponseEntity.noContent().build()))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("read")
    public Mono<ResponseEntity<Object>> markAsRead(@RequestBody List<UUID> ids) {
        return notificationRecipientService.markAsRead(ids)
            .then(Mono.just(ResponseEntity.noContent().build()))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("")
    public Mono<ApiResponse<NotificationRecipientDTO>> getAllUnreadNotifications(@ParameterObject Pageable pageable) {
        log.debug("REST request to get all unread NotificationRecipients by recipientId");
        return notificationRecipientService.findMyUnread(pageable)
            .collectList()
            .zipWith(notificationRecipientService.countMyUnreadNotifications())
            .map(list -> new ApiResponse<>(list.getT1(), list.getT2()));
    }

    @GetMapping("count-unread")
    public Mono<Map<String, Long>> countUnreadNotifications() {
        log.debug("REST request to get the count of all unread NotificationRecipients by recipientId");
        return notificationRecipientService.countMyUnreadNotifications()
            .map(count -> Map.of("count", count));
    }

}
