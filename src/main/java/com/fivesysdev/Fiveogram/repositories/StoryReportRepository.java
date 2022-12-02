package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryReportRepository extends JpaRepository<StoryReport,Long> {
    void deleteByStory_Id(Long id);

    boolean existsByStory_Id(Long id);
}
