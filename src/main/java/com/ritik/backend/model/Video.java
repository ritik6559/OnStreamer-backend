package com.ritik.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String s3Key;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();
//
//    // many videos can be liked by many users
//    @ManyToMany(mappedBy = "likedVideos", fetch = FetchType.LAZY)
//    private Set<User> likedByUsers = new HashSet<>();

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false, length = 2048)
    private String url;

//    @Column(nullable = false, length = 2048)
//    private String thumbnail_url;

    private Long fileSize;

//    private Long likes = 0L;
//
//    private Long dislikes = 0L;

    @CreatedDate
    private LocalDateTime uploadedAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
