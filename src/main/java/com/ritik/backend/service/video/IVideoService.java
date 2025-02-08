package com.ritik.backend.service.video;

import com.ritik.backend.model.Video;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

public interface IVideoService {

    Video uploadVideo(MultipartFile file, String title, String description);

    List<Video> getAllVideos();

    Video getVideoById(Long id);

    StreamingResponseBody streamVideo(String s3Key, HttpServletResponse response);

    String generateUniqueS3Key(String fileName);

    String generatePresignedUrl(String s3Key);
}
