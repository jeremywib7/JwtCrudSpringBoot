package com.j23.server.configuration;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseConfig {

    public static GoogleCredentials GOOGLE_CREDENTIALS;

    public static Storage GOOGLE_CLOUD_STORAGE;

    public static final String BUCKET = "self-service-4820d.appspot.com";

    @PostConstruct
    public void configureFirebaseConnection() throws IOException {

        Resource resource = new ClassPathResource("config/serviceAccountKey.json");

        InputStream serviceAccount = resource.getInputStream();

        GOOGLE_CREDENTIALS = GoogleCredentials.fromStream(serviceAccount);
        GOOGLE_CLOUD_STORAGE = StorageOptions.newBuilder().setCredentials(GOOGLE_CREDENTIALS).build().getService();


        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GOOGLE_CREDENTIALS)
                .setDatabaseUrl("https://self-service-4820d-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        if (!FirebaseApp.getApps().isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


        if (FirebaseApp.getApps().isEmpty()) { //<--- check with this line
            FirebaseApp.initializeApp(options);
        }

    }
}
