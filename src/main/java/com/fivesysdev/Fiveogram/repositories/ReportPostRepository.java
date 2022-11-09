package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.reports.ReportPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportPostRepository extends JpaRepository<ReportPostEntity,Long> {
    void deleteByPost_Id(Long postId);
}
