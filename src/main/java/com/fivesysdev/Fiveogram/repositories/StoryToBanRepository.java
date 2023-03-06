package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import com.fivesysdev.Fiveogram.models.reports.StoryToBan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryToBanRepository extends JpaRepository<StoryToBan,Long> {

    void deleteByStoryReport_Id(Long aLong);
}

