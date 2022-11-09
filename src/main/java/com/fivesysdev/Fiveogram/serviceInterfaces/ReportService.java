package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.PostReport;

import java.util.List;

public interface ReportService {

    List<PostReport> getReports();

    void acceptReport(Long id);

    void declineReport(Long id);
}
