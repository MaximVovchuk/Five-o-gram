package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.reports.ReportStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportStoryRepository extends JpaRepository<ReportStoryEntity,Long> {
    void deleteByStory_Id(Long id);
}
