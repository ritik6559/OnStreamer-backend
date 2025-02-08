package com.ritik.backend.service.video;

import com.ritik.backend.exceptions.ResourceNotFoundException;
import com.ritik.backend.exceptions.VideoStreamingException;
import com.ritik.backend.exceptions.VideoUploadException;
import com.ritik.backend.model.Video;
import com.ritik.backend.repository.VideoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class VideoService implements IVideoService {

    private final String bucketName;
    private final S3Client s3Client;
    private final VideoRepository videoRepository;

    public VideoService(
            @Value("${aws.s3.bucket}") String bucketName,
            S3Client s3Client,
            VideoRepository videoRepository) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
        this.videoRepository = videoRepository;
    }

    @Override
    public Video uploadVideo(MultipartFile file, String title, String description) {
        try {
            String s3Key = generateUniqueS3Key(file.getOriginalFilename());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url = generatePresignedUrl(s3Key);

            Video video = new Video();
            video.setTitle(title);
            video.setDescription(description);
            video.setS3Key(s3Key);
            video.setContentType(file.getContentType());
            video.setFileSize(file.getSize());
            video.setUrl(url);
            return videoRepository.save(video);
        } catch (IOException e) {
            throw new VideoUploadException("Failed to upload: " + e.getMessage());
        }
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }

    public StreamingResponseBody streamVideo(String s3Key, HttpServletResponse response) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            GetObjectResponse getObjectResponse = s3Object.response();

            response.setContentType(getObjectResponse.contentType());
            response.setHeader("Content-Disposition", "inline");

            return outputStream -> {
                try (InputStream inputStream = s3Object; OutputStream os = outputStream) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                        os.flush(); // Flush to ensure continuous streaming
                    }
                }
            };
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to stream video from S3: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateUniqueS3Key(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    @Override
    public String generatePresignedUrl(String s3Key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            Duration maxDuration = Duration.ofDays(7);

            S3Presigner presigner = S3Presigner.builder()
                    .region(s3Client.serviceClientConfiguration().region())
                    .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(maxDuration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            try (presigner) {
                PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);
                return presignedGetObjectRequest.url().toString();
            }

        } catch (S3Exception e) {
            throw new VideoUploadException("Failed to get video url:- " + e.getMessage());
        }
    }
}
