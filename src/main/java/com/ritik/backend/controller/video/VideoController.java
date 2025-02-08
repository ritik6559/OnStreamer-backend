package com.ritik.backend.controller.video;


import com.ritik.backend.dto.VideoDto;
import com.ritik.backend.exceptions.InvalidVideoException;
import com.ritik.backend.exceptions.VideoUploadException;
import com.ritik.backend.model.Video;
import com.ritik.backend.response.ApiResponse;
import com.ritik.backend.service.video.IVideoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/videos")
public class VideoController {

    private final IVideoService videoService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadVideo(@RequestPart MultipartFile file,
                                                   @RequestPart String title,
                                                   @RequestPart String description) {
        try {
            validateVideoFile(file);
            Video video = videoService.uploadVideo(file, title, description);
            return ResponseEntity.ok(new ApiResponse("Success", video));
        } catch (VideoUploadException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failure: " + e.getMessage(), null));
        }
    }


    @GetMapping("/list-videos")
    public ResponseEntity<ApiResponse> getAllVideos() {
        List<VideoDto> videos = videoService.getAllVideos()
                .stream()
                .map(VideoDto::from)
                .toList();
        return ResponseEntity.ok(new ApiResponse("Success", videos));
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable Long id, HttpServletResponse response) {
        try {
            Video video = videoService.getVideoById(id);
            return ResponseEntity.ok(videoService.streamVideo(video.getS3Key(), response));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private void validateVideoFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidVideoException("Video file cannot be empty");
        }
        if (!file.getContentType().startsWith("video/")) {
            throw new InvalidVideoException("File must be a video");
        }
        // 100MB max size
        if (file.getSize() > 100_000_000) {
            throw new InvalidVideoException("File size cannot exceed 100MB");
        }
    }

}
