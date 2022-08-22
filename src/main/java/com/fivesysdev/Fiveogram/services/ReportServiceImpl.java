package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Report;
import com.fivesysdev.Fiveogram.models.ReportLine;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final PostService postService;

    public ReportServiceImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public Report buildReport(User user) {
        List<ReportLine> reportLines = postService.findAll(user).stream()
                .map(post -> new ReportLine(post, post.getLikesList().size()))
                .collect(Collectors.toList());
        return new Report(reportLines);
    }

    @Override
    public Report buildSponsoredReport(User user) {
        List<ReportLine> reportLines = postService.findAll(user).stream()
                .map(post -> new ReportLine(post, post.getLikesList().size()))
                .collect(Collectors.toList());
        return new Report(reportLines);
    }
}
