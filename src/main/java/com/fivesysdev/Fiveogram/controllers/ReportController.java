package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.models.PostReport;
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

    @GetMapping()
    public List<PostReport> getReports(){
        return reportService.getReports();
    }

    @PostMapping("/{id:\\d+}/acceptReport")
    public void acceptReport(@PathVariable Long id){
        reportService.acceptReport(id);
    }

    @PostMapping("/{id:\\d+}/declineReport")
    public void declineReport(@PathVariable Long id){
        reportService.declineReport(id);
    }
}
