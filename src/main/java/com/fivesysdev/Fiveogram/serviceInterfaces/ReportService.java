package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWithThisIdIsNotFound;

import java.util.List;

public interface ReportService {

    void acceptStoryReport(Long id) throws Status451ReportWithThisIdIsNotFound;

    void declineStoryReport(Long id);

    void acceptPostReport(Long id) throws Status451ReportWithThisIdIsNotFound;

    void declinePostReport(Long id);

    List<StoryReportDTO> getStoryReports();

    List<PostReportDTO> getPostReports();
}
