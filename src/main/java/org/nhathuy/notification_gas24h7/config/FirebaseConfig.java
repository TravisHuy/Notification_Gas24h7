package org.nhathuy.notification_gas24h7.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
        InputStream credentialsStream = new ByteArrayInputStream(firebaseCredentials.getBytes());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .setDatabaseUrl(databaseUrl)
                .setStorageBucket(storageBucket)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public StorageClient storageClient() throws IOException {
        return StorageClient.getInstance(firebaseApp());
    }

    @Bean
    public DatabaseReference databaseReference() throws IOException {
        return FirebaseDatabase.getInstance(firebaseApp()).getReference();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
