package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWIthThisIdIsNotFound;

import java.util.List;

public interface ReportService {

    void acceptStoryReport(Long id) throws Status451ReportWIthThisIdIsNotFound;

    void declineStoryReport(Long id);

    void acceptPostReport(Long id) throws Status451ReportWIthThisIdIsNotFound;

    void declinePostReport(Long id);

    List<StoryReportDTO> getStoryReports();

    List<PostReportDTO> getPostReports();
}
