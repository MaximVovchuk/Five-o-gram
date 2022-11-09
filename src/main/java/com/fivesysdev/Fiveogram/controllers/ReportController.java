package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.models.reports.PostReport;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
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
    public List<StoryReport> getStoryReports() {
        return reportService.getStoryReports();
    }

    @PostMapping("/{id:\\d+}/acceptStoryReport")
    public void acceptStoryReport(@PathVariable Long id) {
        reportService.acceptStoryReport(id);
    }

    @PostMapping("/{id:\\d+}/declineStoryReport")
    public void declineStoryReport(@PathVariable Long id) {
        reportService.declineStoryReport(id);
    }

    @GetMapping("/posts")
    public List<PostReport> getPostReports() {
        return reportService.getPostReports();
    }

    @PostMapping("/{id:\\d+}/acceptPostReport")
    public void acceptPostReport(@PathVariable Long id) {
        reportService.acceptPostReport(id);
    }

    @PostMapping("/{id:\\d+}/declinePostReport")
    public void declinePostReport(@PathVariable Long id) {
        reportService.declinePostReport(id);
    }
}
