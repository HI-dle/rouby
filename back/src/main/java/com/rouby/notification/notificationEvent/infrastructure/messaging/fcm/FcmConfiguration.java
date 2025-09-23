package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ApiErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FcmConfiguration {

  private final String FIREBASE_KEY_PATH;

  public FcmConfiguration(@Value("${fcm.key_file_path}") String firebaseKeyPath) {
    this.FIREBASE_KEY_PATH = firebaseKeyPath;
  }

  @Bean
  GoogleCredentials fcmGoogleCredentials() throws IOException {

    ClassPathResource resource = new ClassPathResource(FIREBASE_KEY_PATH);
    try (InputStream serviceAccount = resource.getInputStream()) {
      return GoogleCredentials.fromStream(serviceAccount)
          .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
    }
  }

  @Bean
  FirebaseMessaging firebaseMessaging() {

    try {
      List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
      FirebaseApp firebaseApp = null;

      if (firebaseApps != null && !firebaseApps.isEmpty()) {
        for(FirebaseApp app : firebaseApps){
          if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
            firebaseApp = app;
          }
        }
      } else {

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(fcmGoogleCredentials())
            .build();
        firebaseApp = FirebaseApp.initializeApp(options);
      }

      return FirebaseMessaging.getInstance(Objects.requireNonNull(firebaseApp));

    } catch (IOException e) {
      throw CustomException.from(ApiErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}
