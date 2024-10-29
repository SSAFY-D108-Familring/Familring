package com.familring.userservice.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.path}")
    String path;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(path);

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return FirebaseMessaging.getInstance();
    }
}