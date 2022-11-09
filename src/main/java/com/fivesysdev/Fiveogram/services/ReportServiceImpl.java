package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.reports.PostReport;
import com.fivesysdev.Fiveogram.models.reports.ReportPostEntity;
import com.fivesysdev.Fiveogram.models.reports.ReportStoryEntity;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import com.fivesysdev.Fiveogram.repositories.ReportPostRepository;
import com.fivesysdev.Fiveogram.repositories.ReportStoryRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportPostRepository reportPostRepository;
    private final ReportStoryRepository reportStoryRepository;
    private final PostService postService;
    private final StoryService storyService;

    public ReportServiceImpl(ReportPostRepository reportPostRepository, ReportStoryRepository reportStoryRepository, PostService postService, StoryService storyService) {
        this.reportPostRepository = reportPostRepository;
        this.reportStoryRepository = reportStoryRepository;
        this.postService = postService;
        this.storyService = storyService;
    }

    @Override
    public List<PostReport> getPostReports() {
        List<ReportPostEntity> postReports = reportPostRepository.findAll();
        if (postReports.isEmpty()) {
            return new ArrayList<>();
        }
        List<PostReport> reports = new ArrayList<>();

        postReports.sort((o1, o2) -> Math.toIntExact(o1.getPost().getId() - o2.getPost().getId()));

        Post postWhichWeAreWorkingWith = postReports.get(0).getPost();

        PostReport postReport = new PostReport(postWhichWeAreWorkingWith);

        for (ReportPostEntity report : postReports) {
            if (report.getPost() != postWhichWeAreWorkingWith) {
                postWhichWeAreWorkingWith = report.getPost();
                reports.add(postReport);
                postReport = new PostReport(report.getPost());
            }
            postReport.addReportText(report.getText());
        }
        reports.add(postReport);
        reports.sort(Comparator.comparingInt(o -> o.getReportTexts().size()));
        return reports;
    }

    @Override
    public void acceptPostReport(Long id) {
        postService.banPost(id);
        reportPostRepository.deleteByPost_Id(id);
    }

    @Override
    public void declinePostReport(Long id) {
        reportPostRepository.deleteByPost_Id(id);
    }

    @Override
    public List<StoryReport> getStoryReports() {
        List<ReportStoryEntity> storyReports = reportStoryRepository.findAll();
        if (storyReports.isEmpty()) {
            return new ArrayList<>();
        }
        List<StoryReport> reports = new ArrayList<>();

        storyReports.sort((o1, o2) -> Math.toIntExact(o1.getStory().getId() - o2.getStory().getId()));

        Story storyWhichWeAreWorkingWith = storyReports.get(0).getStory();

        StoryReport storyReport = new StoryReport(storyWhichWeAreWorkingWith);

        for (ReportStoryEntity report : storyReports) {
            if (report.getStory() != storyWhichWeAreWorkingWith) {
                storyWhichWeAreWorkingWith = report.getStory();
                reports.add(storyReport);
                storyReport = new StoryReport(report.getStory());
            }
            storyReport.addReportText(report.getText());
        }
        reports.add(storyReport);
        reports.sort(Comparator.comparingInt(o -> o.getReportTexts().size()));
        return reports;
    }

    @Override
    public void acceptStoryReport(Long id) {
        storyService.banPost(id);
        reportStoryRepository.deleteByStory_Id(id);
    }

    @Override
    public void declineStoryReport(Long id) {
        reportStoryRepository.deleteByStory_Id(id);
    }

}