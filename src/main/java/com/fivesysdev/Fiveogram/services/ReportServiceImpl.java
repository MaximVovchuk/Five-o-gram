package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status451ReportWIthThisIdIsNotFound;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.models.reports.PostReport;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.repositories.PostReportRepository;
import com.fivesysdev.Fiveogram.repositories.StoryReportRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final PostReportRepository postReportRepository;
    private final StoryReportRepository storyReportRepository;
    private final PostService postService;
    private final StoryService storyService;

    public ReportServiceImpl(PostReportRepository postReportRepository, StoryReportRepository storyReportRepository, PostService postService, StoryService storyService) {
        this.postReportRepository = postReportRepository;
        this.storyReportRepository = storyReportRepository;
        this.postService = postService;
        this.storyService = storyService;
    }

    @Override
    public List<PostReportDTO> getPostReports() {
        List<PostReport> postReports = postReportRepository.findAll();
        if (postReports.isEmpty()) {
            return new ArrayList<>();
        }
        List<PostReportDTO> reports = new ArrayList<>();

        postReports.sort((o1, o2) -> Math.toIntExact(o1.getPost().getId() - o2.getPost().getId()));

        Post postWhichWeAreWorkingWith = postReports.get(0).getPost();

        PostReportDTO postReportDTO = new PostReportDTO(postWhichWeAreWorkingWith);

        for (PostReport report : postReports) {
            if (report.getPost() != postWhichWeAreWorkingWith) {
                postWhichWeAreWorkingWith = report.getPost();
                reports.add(postReportDTO);
                postReportDTO = new PostReportDTO(report.getPost());
            }
            postReportDTO.addReportText(report.getText());
        }
        reports.add(postReportDTO);
        reports.sort(Comparator.comparingInt(o -> o.getReportTexts().size()));
        return reports;
    }

    @Override
    public void acceptPostReport(Long id) throws Status451ReportWIthThisIdIsNotFound {
        if(!postReportRepository.existsByPost_Id(id)) {
            throw new Status451ReportWIthThisIdIsNotFound();
        }
        postReportRepository.deleteByPost_Id(id);
        postService.banPost(id);
    }

    @Override
    public void declinePostReport(Long id) {
        postReportRepository.deleteByPost_Id(id);
    }

    @Override
    public List<StoryReportDTO> getStoryReports() {
        List<StoryReport> storyReports = storyReportRepository.findAll();
        if (storyReports.isEmpty()) {
            return new ArrayList<>();
        }
        List<StoryReportDTO> reports = new ArrayList<>();

        storyReports.sort((o1, o2) -> Math.toIntExact(o1.getStory().getId() - o2.getStory().getId()));

        Story storyWhichWeAreWorkingWith = storyReports.get(0).getStory();

        StoryReportDTO storyReportDTO = new StoryReportDTO(storyWhichWeAreWorkingWith);

        for (StoryReport report : storyReports) {
            if (report.getStory() != storyWhichWeAreWorkingWith) {
                storyWhichWeAreWorkingWith = report.getStory();
                reports.add(storyReportDTO);
                storyReportDTO = new StoryReportDTO(report.getStory());
            }
            storyReportDTO.addReportText(report.getText());
        }
        reports.add(storyReportDTO);
        reports.sort(Comparator.comparingInt(o -> o.getReportTexts().size()));
        return reports;
    }

    @Override
    public void acceptStoryReport(Long id) throws Status451ReportWIthThisIdIsNotFound {
        if(!storyReportRepository.existsByStory_Id(id)) {
            throw new Status451ReportWIthThisIdIsNotFound();
        }
        storyReportRepository.deleteByStory_Id(id);
        storyService.banStory(id);
    }

    @Override
    public void declineStoryReport(Long id) {
        storyReportRepository.deleteByStory_Id(id);
    }

}