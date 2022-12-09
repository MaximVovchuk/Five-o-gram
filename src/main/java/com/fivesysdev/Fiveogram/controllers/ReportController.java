package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWIthThisIdIsNotFound;
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

    @PostMapping("/{id}/acceptStoryReport")
    public void acceptStoryReport(@PathVariable Long id) throws Status451ReportWIthThisIdIsNotFound {
        reportService.acceptStoryReport(id);
    }

    @PostMapping("/{id}/declineStoryReport")
    public void declineStoryReport(@PathVariable Long id) {
        reportService.declineStoryReport(id);
    }

    @GetMapping("/posts")
    public Response<List<PostReportDTO>> getPostReports() {
        return new Response<>(reportService.getPostReports());
    }

    @PostMapping("/{id}/acceptPostReport")
    public void acceptPostReport(@PathVariable Long id) throws Status451ReportWIthThisIdIsNotFound {
        reportService.acceptPostReport(id);
    }

    @PostMapping("/{id}/declinePostReport")
    public void declinePostReport(@PathVariable Long id) {
        reportService.declinePostReport(id);
    }
}
