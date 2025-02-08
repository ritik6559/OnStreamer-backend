package com.ritik.backend.repository;

import com.ritik.backend.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
