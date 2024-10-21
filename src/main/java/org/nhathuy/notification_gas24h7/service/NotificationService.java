package org.nhathuy.notification_gas24h7.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.nhathuy.notification_gas24h7.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {
    private final DatabaseReference database;
    private final FirebaseMessaging fcm;
    private final ImageUploadService imageUploadService;

    @Autowired
    public NotificationService(DatabaseReference firebaseDatabase, FirebaseMessaging fcm, ImageUploadService imageUploadService) {
        this.database = firebaseDatabase.getDatabase().getReference("notifications");
        this.fcm = fcm;
        this.imageUploadService = imageUploadService;
    }

    public Notification createNotification(Notification notification, MultipartFile image) throws IOException {
        String imageUrl = uploadImage(image);

        //set notification properties
        String key = database.push().getKey();
        notification.setId(key);
        notification.setImageUrl(imageUrl);
        notification.setCreateAt(new Date());

        //save notification to firebase
        database.child(key).setValueAsync(notification);


        //send notification to users
        sendNotification(notification);

        return notification;
    }

    private String uploadImage(MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            return imageUploadService.uploadFile(image);
        }
        return null;
    }

    private void sendNotification(Notification notification) {
        Message message = Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setBody(notification.getBody())
                        .setTitle(notification.getTitle())
                        .setImage(notification.getImageUrl())
                        .build())
                .setTopic("all_user")
                .build();
        try{
            String reponse = fcm.send(message);
            System.out.println("Successfully sent message: "+reponse);
        }
        catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getAllNotifications(){
        List<Notification> notifications = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Notification notification = snapshot.getValue(Notification.class);
                    notifications.add(notification);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error fetching notifications: " + databaseError.getMessage());
            }
        });

        return notifications;
    }
    public Notification getNotificationById(String id){
        final Notification[] notification={null};
        database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notification[0] =dataSnapshot.getValue(Notification.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error fetching notifications: " + databaseError.getMessage());
            }
        });
        return notification[0];
    }
}