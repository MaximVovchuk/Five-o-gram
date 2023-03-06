package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.reports.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    int countAllByPost_Id(Long post_id);
    List<PostReport> findAllByPost(Post post);
    void deleteAllByPost_Id(Long post_id);
    List<PostReport> findByPost_Id(Long post_id);

    boolean existsByPost_Id(Long postId);
}
