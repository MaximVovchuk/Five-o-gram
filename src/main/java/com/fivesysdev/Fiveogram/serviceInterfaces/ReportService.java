package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Report;
import com.fivesysdev.Fiveogram.models.ReportLine;
import com.fivesysdev.Fiveogram.models.User;

import java.util.List;
import java.util.stream.Collectors;

public interface ReportService {
    Report buildReport(User user);

    Report buildSponsoredReport(User user);
}
