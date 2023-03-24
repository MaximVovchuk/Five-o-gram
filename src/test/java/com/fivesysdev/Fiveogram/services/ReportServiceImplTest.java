package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.PostReportDTO;
import com.fivesysdev.Fiveogram.dto.StoryReportDTO;
import com.fivesysdev.Fiveogram.exceptions.Status451ReportWithThisIdIsNotFound;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.reports.PostReport;
import com.fivesysdev.Fiveogram.models.reports.PostsToBan;
import com.fivesysdev.Fiveogram.models.reports.StoryReport;
import com.fivesysdev.Fiveogram.models.reports.StoryToBan;
import com.fivesysdev.Fiveogram.repositories.PostReportRepository;
import com.fivesysdev.Fiveogram.repositories.PostsToBanRepository;
import com.fivesysdev.Fiveogram.repositories.StoryReportRepository;
import com.fivesysdev.Fiveogram.repositories.StoryToBanRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {
    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private PostReportRepository postReportRepository;
    @Mock
    private PostsToBanRepository postsToBanRepository;

    @Mock
    private StoryReportRepository storyReportRepository;
    @Mock
    private StoryToBanRepository storyToBanRepository;
    @Mock
    private PostService postService;

    @Mock
    private StoryService storyService;

    @Test
    public void getPostReports_shouldReturnEmptyList_whenNoReportsFound() {
        when(postsToBanRepository.findAll()).thenReturn(Collections.emptyList());
        List<PostReportDTO> postReports = reportService.getPostReports();
        assertThat(postReports).isEmpty();
    }
    @Test
    public void getPostReports_whenPostReportsArePresent_returnsSortedPostReportDTOs() {
        // Arrange
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post1.setId(2L);
        List<PostsToBan> postReports = Arrays.asList(
                new PostsToBan(new PostReport(post1, "Report 1")),
                new PostsToBan(new PostReport(post1, "Report 2")),
                new PostsToBan(new PostReport(post2, "Report 3"))
        );
        when(postsToBanRepository.findAll()).thenReturn(postReports);

        // Act
        List<PostReportDTO> result = reportService.getPostReports();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).reportTexts().size());
        assertEquals(2, result.get(1).reportTexts().size());
        assertEquals("Report 3", result.get(0).reportTexts().get(0));
        assertEquals("Report 2", result.get(1).reportTexts().get(1));
        assertEquals("Report 1", result.get(1).reportTexts().get(0));
    }

    @Test
    public void getPostReports_whenPostReportsAreEmpty_returnsEmptyList() {
        // Arrange
        when(postsToBanRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<PostReportDTO> result = reportService.getPostReports();

        // Assert
        assertEquals(0, result.size());
    }

//    @Test
//    public void acceptPostReport_shouldCallBanPost_whenReportFound() throws Status451ReportWithThisIdIsNotFound {
//        Long id = 1L;
//        when(postReportRepository.existsByPost_Id(id)).thenReturn(true);
//        reportService.acceptPostReport(id);
//        verify(postService, Mockito.times(1)).banPost(id);
//    }

    @Test
    public void acceptPostReport_shouldThrowException_whenReportNotFound() {
        Long id = 1L;
        when(postReportRepository.existsByPost_Id(id)).thenReturn(false);
        assertThrows(Status451ReportWithThisIdIsNotFound.class, () -> reportService.acceptPostReport(id));
    }

//    @Test
//    public void declinePostReport_shouldCallDeleteByPost_Id() {
//        Long id = 1L;
//        reportService.declinePostReport(id);
//        verify(postReportRepository, Mockito.times(1)).deleteByPost_Id(id);
//    }

    @Test
    public void testGetStoryReports() {
        StoryReport storyReport = new StoryReport();
        Story story = new Story();
        story.setId(1L);
        storyReport.setStory(story);
        storyReport.setText("Report text");
        StoryReport storyReport1 = new StoryReport(story, "Lol");
        when(storyToBanRepository.findAll()).thenReturn(
                new ArrayList<>(List.of(new StoryToBan(storyReport), new StoryToBan(storyReport1))));
        List<StoryReportDTO> storyReports = reportService.getStoryReports();
        assertEquals(1, storyReports.size());
        assertEquals("Report text", storyReports.get(0).reportTexts().get(0));
        assertEquals("Lol", storyReports.get(0).reportTexts().get(1));
    }

    @Test
    public void testGetStoryReportsAreEmpty(){
        when(storyToBanRepository.findAll()).thenReturn(new ArrayList<>());
        assertThat(reportService.getStoryReports()).isEmpty();
    }
    @Test
    public void acceptStoryReport_whenStoryReportExists_deletesReportAndBansStory() throws Status451ReportWithThisIdIsNotFound {
        // Arrange
        Long id = 1L;
        Story story = new Story();
        story.setId(id);
        when(storyReportRepository.existsByStory_Id(id)).thenReturn(true);

        // Act
        reportService.acceptStoryReport(id);

        // Assert
        verify(storyReportRepository).existsByStory_Id(id);
        verify(storyReportRepository).deleteAllByStory_Id(id);
        verify(storyService).banStory(id);
    }

    @Test
    public void acceptStoryReport_whenStoryReportDoesNotExist_throwsException() {
        // Arrange
        Long id = 1L;
        when(storyReportRepository.existsByStory_Id(id)).thenReturn(false);

        // Act & Assert
        assertThrows(Status451ReportWithThisIdIsNotFound.class,
                () -> reportService.acceptStoryReport(id));
        verify(storyReportRepository).existsByStory_Id(id);
        verify(storyReportRepository, never()).deleteByStory_Id(id);
        verify(storyService, never()).banStory(id);
    }

//    @Test
//    public void declineStoryReport_deletesStoryReport() throws Status451ReportWithThisIdIsNotFound {
//        // Arrange
//        Long id = 1L;
//
//        // Act
//        reportService.declineStoryReport(id);
//
//        // Assert
//        verify(storyReportRepository).deleteByStory_Id(id);
//    }

}