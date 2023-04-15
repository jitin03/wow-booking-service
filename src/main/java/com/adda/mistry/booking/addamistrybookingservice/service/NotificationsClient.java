package com.adda.mistry.booking.addamistrybookingservice.service;


import com.adda.mistry.booking.addamistrybookingservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATIONS-SERVICE/api/v1/notifications")
public interface NotificationsClient {


    @PostMapping
    public ResponseEntity<String> saveNotification(@RequestBody NotificationRequest notificationRequest);
}
