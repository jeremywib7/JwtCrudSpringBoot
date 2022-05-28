package com.j23.server.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
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

    @PostConstruct
    public void configureFirebaseConnection() throws IOException {

//    File file = ResourceUtils.getFile("classpath:config/serviceAccountKey.json");

//        File file = new File(Objects.requireNonNull(getClass().getResource("/config/serviceAccountKey.json")).getFile());

//        FileInputStream serviceAccount = new FileInputStream(file);

        Resource resource = new ClassPathResource("config/serviceAccountKey.json");

        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
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
