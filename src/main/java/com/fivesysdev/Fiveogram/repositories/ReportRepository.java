package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report,Long> {
    void deleteByPost_Id(Long postId);
}
