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

@Configuration
public class FirebaseConfig {

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${firebase.storage.bucket}")
    private String storageBucket;


    @Bean
    public FirebaseApp firebaseApp() throws IOException{

        JSONObject firebaseCredentials = new JSONObject();
        firebaseCredentials.put("type", System.getenv("FIREBASE_TYPE"));
        firebaseCredentials.put("project_id", System.getenv("FIREBASE_PROJECT_ID"));
        firebaseCredentials.put("private_key_id", System.getenv("FIREBASE_PRIVATE_KEY_ID"));
        firebaseCredentials.put("private_key", System.getenv("FIREBASE_PRIVATE_KEY").replace("\\n", "\n"));
        firebaseCredentials.put("client_email", System.getenv("FIREBASE_CLIENT_EMAIL"));
        firebaseCredentials.put("client_id", System.getenv("FIREBASE_CLIENT_ID"));
        firebaseCredentials.put("auth_uri", System.getenv("FIREBASE_AUTH_URI"));
        firebaseCredentials.put("token_uri", System.getenv("FIREBASE_TOKEN_URI"));
        firebaseCredentials.put("auth_provider_x509_cert_url", System.getenv("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
        firebaseCredentials.put("client_x509_cert_url", System.getenv("FIREBASE_CLIENT_X509_CERT_URL"));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseCredentials.toString().getBytes())))
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
    public FirebaseMessaging firebaseMessaging() throws IOException{
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
