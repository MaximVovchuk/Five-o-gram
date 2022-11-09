package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.reports.PostReport;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;

import java.util.List;

public interface ReportService {

    void acceptStoryReport(Long id);

    void declineStoryReport(Long id);

    void acceptPostReport(Long id);

    void declinePostReport(Long id);

    List<StoryReport> getStoryReports();

    List<PostReport> getPostReports();
}
