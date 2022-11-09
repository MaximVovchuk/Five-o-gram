package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.ReportPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<ReportPostEntity,Long> {
    void deleteByPost_Id(Long postId);
}
