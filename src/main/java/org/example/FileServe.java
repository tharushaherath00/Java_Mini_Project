package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class ClientCredentials {
    private final String clientName;
    private final String clientSecret;

    public ClientCredentials(String clientName, String clientSecret) {
        this.clientName = clientName;
        this.clientSecret = clientSecret;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}

interface FileService {
    String signup(ClientCredentials credentials) throws FileServeException;
    String login(ClientCredentials credentials) throws FileServeException;
    String uploadFile(File file, String token, String courseId) throws FileServeException;
    String requestFileLink(String fileId, String token) throws FileServeException;
    void downloadFile(String url, String fileId, String outputDir) throws FileServeException;
}

public class FileServe implements FileService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FileServe() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String signup(ClientCredentials credentials) throws FileServeException {
        try {
            HttpPost request = new HttpPost(BASE_URL + "/signup");
            request.setHeader("Content-Type", "application/json");

            Map<String, String> payload = new HashMap<>();
            payload.put("client_name", credentials.getClientName());
            payload.put("client_secret", credentials.getClientSecret());

            request.setEntity(new StringEntity(objectMapper.writeValueAsString(payload), ContentType.APPLICATION_JSON));

            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (status == 201) {
                    Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                    return result.get("client_id").toString();
                }
                try {
                    throw new FileServeException("Signup failed: " + responseBody);
                } catch (FileServeException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new FileServeException("Signup request failed", e);
        }
    }

    @Override
    public String login(ClientCredentials credentials) throws FileServeException {
        try {
            HttpPost request = new HttpPost(BASE_URL + "/login");
            request.setHeader("Content-Type", "application/json");

            Map<String, String> payload = new HashMap<>();
            payload.put("client_name", credentials.getClientName());
            payload.put("client_secret", credentials.getClientSecret());

            request.setEntity(new StringEntity(objectMapper.writeValueAsString(payload), ContentType.APPLICATION_JSON));

            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (status == 200) {
                    Map<String, String> result = objectMapper.readValue(responseBody, Map.class);
                    return result.get("token");
                }
                try {
                    throw new FileServeException("Login failed: " + responseBody);
                } catch (FileServeException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new FileServeException("Login request failed", e);
        }
    }

    @Override
    public String uploadFile(File file, String token, String courseId) throws FileServeException {
        if (!isValidCourseId(courseId)) {
            throw new FileServeException("Invalid Course ID: " + courseId);
        }

        try {
            HttpPost request = new HttpPost(BASE_URL + "/upload_file");
            request.setHeader("Authorization", "Bearer " + token);

            // multipart entity MultipartEntityBuilder
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                    .build();
            request.setEntity(entity);

            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (status == 201) {
                    Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                    String fileId = result.get("file_id").toString();

                    try {
                        storeFileMetadata(fileId, file, courseId);
                    } catch (FileServeException e) {
                        throw new RuntimeException(e);
                    }

                    return fileId;
                }
                try {
                    throw new FileServeException("File upload failed: " + responseBody);
                } catch (FileServeException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new FileServeException("File upload request failed", e);
        }
    }

    @Override
    public String requestFileLink(String fileId, String token) throws FileServeException {
        try {
            HttpPost request = new HttpPost(BASE_URL + "/request_file");
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");

            Map<String, String> payload = new HashMap<>();
            payload.put("file_id", fileId);

            request.setEntity(new StringEntity(objectMapper.writeValueAsString(payload), ContentType.APPLICATION_JSON));

            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (status == 200) {
                    Map<String, String> result = objectMapper.readValue(responseBody, Map.class);
                    return result.get("url");
                }
                try {
                    throw new FileServeException("File link request failed: " + responseBody);
                } catch (FileServeException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new FileServeException("File link request failed", e);
        }
    }

    @Override
    public void downloadFile(String url, String fileId, String outputDir) throws FileServeException {
        String originalFilename = getOriginalFilename(fileId);
        if (originalFilename == null) {
            throw new FileServeException("No file found for fileId: " + fileId);
        }

        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists() && !outputDirFile.mkdirs()) {
            throw new FileServeException("Failed to create output directory: " + outputDir);
        }
        String outputPath = new File(outputDir, originalFilename).getAbsolutePath();

        try {
            HttpGet request = new HttpGet(url);

            httpClient.execute(request, response -> {
                int status = response.getCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    try (FileOutputStream out = new FileOutputStream(outputPath)) {
                        entity.writeTo(out);
                    }
                    return null;
                }
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                try {
                    throw new FileServeException("File download failed: " + responseBody);
                } catch (FileServeException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new FileServeException("File download request failed", e);
        }
    }

    private void storeFileMetadata(String fileId, File file, String courseId) throws FileServeException {
        String sql = "INSERT INTO files (file_id, filename, course_id) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileId);
            pstmt.setString(2, file.getName());
            pstmt.setString(3, courseId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new FileServeException("Failed to store file metadata", e);
        }
    }

    private boolean isValidCourseId(String courseId) throws FileServeException {
        String sql = "SELECT COUNT(*) FROM Course WHERE Course_ID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new FileServeException("Failed to validate course ID", e);
        }
    }

    private String getOriginalFilename(String fileId) throws FileServeException {
        String sql = "SELECT filename FROM files WHERE file_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("filename");
            }
            return null;
        } catch (SQLException e) {
            throw new FileServeException("Failed to retrieve original filename", e);
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException _) {

        }
    }
}