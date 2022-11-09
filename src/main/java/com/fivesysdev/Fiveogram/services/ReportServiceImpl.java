package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.PostReport;
import com.fivesysdev.Fiveogram.models.ReportPostEntity;
import com.fivesysdev.Fiveogram.repositories.ReportRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final PostService postService;

    public ReportServiceImpl(ReportRepository reportRepository, PostService postService) {
        this.reportRepository = reportRepository;
        this.postService = postService;
    }

    @Override
    public List<PostReport> getReports() {
        List<ReportPostEntity> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            return new ArrayList<>();
        }
        List<PostReport> postReports = new ArrayList<>();

        reports.sort((o1, o2) -> Math.toIntExact(o1.getPost().getId() - o2.getPost().getId()));

        Post postWhichWeAreWorkingWith = reports.get(0).getPost();

        PostReport postReport = new PostReport();

        postReport.setPost(reports.get(0).getPost());

        for (ReportPostEntity report : reports) {
            if (report.getPost() != postWhichWeAreWorkingWith) {
                postWhichWeAreWorkingWith = report.getPost();
                postReports.add(postReport);
                postReport = new PostReport();
                postReport.setPost(report.getPost());
            }
            postReport.addReportText(report);
        }
        postReports.add(postReport);
        postReports.sort(Comparator.comparingInt(o -> o.getReportTexts().size()));
        return postReports;
    }

    @Override
    public void acceptReport(Long id) {
        postService.banPost(id);
        reportRepository.deleteByPost_Id(id);
    }

    @Override
    public void declineReport(Long id) {
        reportRepository.deleteByPost_Id(id);
    }
}