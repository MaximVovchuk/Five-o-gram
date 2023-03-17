package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWithThisIdIsNotFound;
import com.fivesysdev.Fiveogram.models.ReportStatus;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import com.fivesysdev.Fiveogram.util.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/stories")
    public Response<List<StoryReportDTO>> getStoryReports() {
        return new Response<>(reportService.getStoryReports());
    }

    @PostMapping("/story/{id}")
    public void responseToStoryReport(@PathVariable Long id, @RequestParam ReportStatus reportStatus)
            throws Status451ReportWithThisIdIsNotFound {
        reportService.responseToStoryReport(id, reportStatus);
    }

    @GetMapping("/posts")
    public Response<List<PostReportDTO>> getPostReports() {
        return new Response<>(reportService.getPostReports());
    }

    @PostMapping("/post/{id}")
    public void responseToPostReport(@PathVariable Long id, @RequestParam ReportStatus reportStatus)
            throws Status451ReportWithThisIdIsNotFound {
        reportService.responseToPostReport(id, reportStatus);
    }
}
