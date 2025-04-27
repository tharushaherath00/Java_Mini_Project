package org.example;

import java.io.File;

public class FileServeDemo {
    public static void main(String[] args) {
        FileServe fileServe = new FileServe();

        try {
            // Signup
            ClientCredentials credentials = new ClientCredentials("test_backend", "your_random_jwt_secret_32_chars_minimum");
            String clientId = fileServe.signup(credentials);

            // Login
            String token = fileServe.login(credentials);

            // Upload file for a specific course
            File file = new File("D:/azure.pdf");
            String courseId = "ICT2113";
            String fileId = fileServe.uploadFile(file, token, courseId);

            // Request file link
            String downloadUrl = fileServe.requestFileLink(fileId, token);

            String outputDir = "."; // in root of the porject
            fileServe.downloadFile(downloadUrl, fileId, outputDir);

        } catch (FileServeException e) {
            e.printStackTrace();
        } finally {
            fileServe.close();
        }
    }
}