package org.sevosmart.com.sevosmartbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FirebaseConfig {
    public static void initializeFirebase() throws Exception {
        String base64Key = System.getenv("FIREBASE_PRIVATE_KEY");
        byte[] decodedBytes = Base64.getDecoder().decode(base64Key);
        String tempFilePath = "firebase-private-key.json";

        try (FileOutputStream fos = new FileOutputStream(tempFilePath)) {
            fos.write(decodedBytes);
        }

        // Initialize Firebase with the temporary file
        FileInputStream serviceAccount = new FileInputStream(tempFilePath);
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        FirebaseApp.initializeApp(options);

        // Delete the temporary file after use
        Files.delete(Paths.get(tempFilePath));
    }
}
