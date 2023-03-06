package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryReportRepository extends JpaRepository<StoryReport,Long> {
    StoryReport deleteByStory_Id(Long id);
    int countAllByStory_Id(Long id);

    boolean existsByStory_Id(Long id);
    List<StoryReport> findAllByStory(Story story);

    List<StoryReport> findByStory_Id(Long id);

    void deleteAllByStory_Id(Long id);
}
