package com.ritik.backend.dto;

import com.ritik.backend.model.Video;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class VideoDto {
    private Long id;
    private String title;
    private String description;
    private String url;
    private Long fileSize;
    private LocalDateTime uploadDate;

    public static VideoDto from(Video video) {
        return VideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .url(video.getUrl())
                .fileSize(video.getFileSize())
                .uploadDate(video.getUploadedAt())

                .build();
    }
}
