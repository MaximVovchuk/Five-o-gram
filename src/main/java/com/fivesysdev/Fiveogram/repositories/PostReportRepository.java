package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.reports.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostReportRepository extends JpaRepository<PostReport,Long> {

    void deleteByPost_Id(Long postId);
    boolean existsByPost_Id(Long postId);
}
