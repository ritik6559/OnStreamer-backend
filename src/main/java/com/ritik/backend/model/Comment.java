//package com.ritik.backend.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
/// /@Entity(name = "comments")
//public class Comment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String content;
//
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    // many comments belong to one user
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//
//    // many videos belong to one user
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "video_id", nullable = false)
//    private Video video;
//}
