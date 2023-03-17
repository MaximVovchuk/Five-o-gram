package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status445StoryNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWithThisIdIsNotFound;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.ReportStatus;
import com.fivesysdev.Fiveogram.models.Story;

import java.util.List;

public interface ReportService {
    Post reportPost(Long id, String text) throws Status435PostNotFoundException;

    Story reportStory(Long id, String text) throws Status445StoryNotFoundException;

    void responseToStoryReport(Long id, ReportStatus reportStatus) throws Status451ReportWithThisIdIsNotFound;

    void responseToPostReport(Long id, ReportStatus reportStatus) throws Status451ReportWithThisIdIsNotFound;

    List<StoryReportDTO> getStoryReports();

    List<PostReportDTO> getPostReports();
}
