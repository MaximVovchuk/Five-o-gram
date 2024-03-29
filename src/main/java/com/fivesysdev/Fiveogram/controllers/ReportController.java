package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWithThisIdIsNotFound;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import com.fivesysdev.Fiveogram.util.Response;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@Api(value = "Report endpoints", tags = {"Report"})
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
    public void acceptStoryReport(@PathVariable Long id) throws Status451ReportWithThisIdIsNotFound {
        reportService.acceptStoryReport(id);
    }

    @PostMapping("/{id}/declineStoryReport")
    public void declineStoryReport(@PathVariable Long id) throws Status451ReportWithThisIdIsNotFound {
        reportService.declineStoryReport(id);
    }

    @GetMapping("/posts")
    public Response<List<PostReportDTO>> getPostReports() {
        return new Response<>(reportService.getPostReports());
    }

    @PostMapping("/{id}/acceptPostReport")
    public void acceptPostReport(@PathVariable Long id) throws Status451ReportWithThisIdIsNotFound {
        reportService.acceptPostReport(id);
    }

    @PostMapping("/{id}/declinePostReport")
    public void declinePostReport(@PathVariable Long id) throws Status451ReportWithThisIdIsNotFound {
        reportService.declinePostReport(id);
    }
}
