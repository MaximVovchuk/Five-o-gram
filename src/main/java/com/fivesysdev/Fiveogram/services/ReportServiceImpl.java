package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status445StoryNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWithThisIdIsNotFound;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.reports.PostReport;
import com.fivesysdev.Fiveogram.models.reports.PostsToBan;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import com.fivesysdev.Fiveogram.models.reports.StoryToBan;
import com.fivesysdev.Fiveogram.repositories.*;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final PostsToBanRepository postsToBanRepository;
    private final StoryReportRepository storyReportRepository;
    private final StoryToBanRepository storyToBanRepository;
    private final PostService postService;
    private final StoryService storyService;

    @Override
    public List<PostReportDTO> getPostReports() {
        List<PostsToBan> postsToBan = postsToBanRepository.findAll();
        if (postsToBan.isEmpty()) {
            return new ArrayList<>();
        }
        List<PostReportDTO> reports = new ArrayList<>();

        postsToBan.sort((o1, o2) -> Math.toIntExact
                (o1.getPostReport().getPost().getId() - o2.getPostReport().getPost().getId()));

        Post postWhichWeAreWorkingWith = postsToBan.get(0).getPostReport().getPost();

        PostReportDTO postReportDTO = new PostReportDTO(postWhichWeAreWorkingWith);

        for (PostsToBan postToBan : postsToBan) {
            PostReport report = postToBan.getPostReport();
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
    public Post reportPost(Long id, String text) throws Status435PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(Status435PostNotFoundException::new);
        PostReport postReport = PostReport.builder()
                .text(text)
                .post(post)
                .build();
        postReportRepository.save(postReport);
        additionalPostReport(postReport);
        return post;
    }

    private void additionalPostReport(PostReport postReport) {
        int thisPostReportsCount = postReportRepository.countAllByPost_Id(postReport.getPost().getId());
        if (thisPostReportsCount == 10) {
            postsToBanRepository.saveAll(
                    postReportRepository.findAllByPost(postReport.getPost())
                            .stream().map(PostsToBan::new).collect(Collectors.toList()));
        } else if (thisPostReportsCount > 10) {
            postsToBanRepository.save(new PostsToBan(postReport));
        }
    }

    @Override
    @Transactional
    public void acceptPostReport(Long id) throws Status451ReportWithThisIdIsNotFound {
        if (!postReportRepository.existsByPost_Id(id)) {
            throw new Status451ReportWithThisIdIsNotFound();
        }
        postReportRepository.findByPost_Id(id).stream().map(PostReport::getId)
                .forEach(postsToBanRepository::deleteByPostReport_Id);
        postReportRepository.deleteAllByPost_Id(id);
        postService.banPost(id);
    }

    @Override
    @Transactional
    public void declinePostReport(Long id) throws Status451ReportWithThisIdIsNotFound {
        if (!postReportRepository.existsByPost_Id(id)) {
            throw new Status451ReportWithThisIdIsNotFound();
        }
        postReportRepository.findByPost_Id(id).stream().map(PostReport::getId)
                .forEach(postsToBanRepository::deleteByPostReport_Id);
        postReportRepository.deleteAllByPost_Id(id);
    }

    @Override
    public List<StoryReportDTO> getStoryReports() {
        List<StoryToBan> storiesToBan = storyToBanRepository.findAll();
        if (storiesToBan.isEmpty()) {
            return new ArrayList<>();
        }
        List<StoryReportDTO> reports = new ArrayList<>();

        storiesToBan.sort((o1, o2) -> Math.toIntExact(o1.getStoryReport().getStory().getId() - o2.getStoryReport().getStory().getId()));

        Story storyWhichWeAreWorkingWith = storiesToBan.get(0).getStoryReport().getStory();

        StoryReportDTO storyReportDTO = new StoryReportDTO(storyWhichWeAreWorkingWith);

        for (StoryToBan story : storiesToBan) {
            StoryReport report = story.getStoryReport();
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
    public Story reportStory(Long id, String text) throws Status445StoryNotFoundException {
        Story story = storyService.getStoryById(id);
        StoryReport storyReport = StoryReport.builder()
                .story(story)
                .text(text)
                .build();
        storyReportRepository.save(storyReport);
        additionalStoryReport(storyReport);
        return story;
    }

    private void additionalStoryReport(StoryReport storyReport) {
        int thisStoryReportsCount = storyReportRepository.countAllByStory_Id(storyReport.getStory().getId());
        if (thisStoryReportsCount == 10) {
            storyToBanRepository.saveAll(
                    storyReportRepository.findAllByStory(storyReport.getStory())
                            .stream().map(StoryToBan::new).collect(Collectors.toList()));
        } else if (thisStoryReportsCount > 10) {
            storyToBanRepository.save(new StoryToBan(storyReport));
        }
    }
    @Override
    @Transactional
    public void acceptStoryReport(Long id) throws Status451ReportWithThisIdIsNotFound {
        if (!storyReportRepository.existsByStory_Id(id)) {
            throw new Status451ReportWithThisIdIsNotFound();
        }
        storyReportRepository.findByStory_Id(id).stream().map(StoryReport::getId)
                .forEach(storyToBanRepository::deleteByStoryReport_Id);
        storyReportRepository.deleteAllByStory_Id(id);
        storyService.banStory(id);
    }

    @Override
    @Transactional
    public void declineStoryReport(Long id) throws Status451ReportWithThisIdIsNotFound {
        if (!storyReportRepository.existsByStory_Id(id)) {
            throw new Status451ReportWithThisIdIsNotFound();
        }
        storyReportRepository.findByStory_Id(id).stream().map(StoryReport::getId)
                .forEach(storyToBanRepository::deleteByStoryReport_Id);
        storyReportRepository.deleteAllByStory_Id(id);
    }

}