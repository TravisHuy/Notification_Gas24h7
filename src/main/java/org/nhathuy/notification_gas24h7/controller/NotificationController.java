package org.nhathuy.notification_gas24h7.controller;

import org.nhathuy.notification_gas24h7.model.Notification;
import org.nhathuy.notification_gas24h7.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<String> getNotifications() {
        return ResponseEntity.ok("Hello World");
    }


    @Autowired
    public NotificationController(NotificationService notificationService){
        this.notificationService=notificationService;
    }
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(
            @RequestParam("notification") Notification notification,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Notification savedNotification = notificationService.createNotification(notification, image);
        return ResponseEntity.ok(savedNotification);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotification(){
        List<Notification> notifications=notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id){
        Notification notification=notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
}
