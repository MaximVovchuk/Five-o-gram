package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Filter;
import com.fivesysdev.Fiveogram.models.Picture;

import java.util.List;

public interface PreparationService {
    Picture prepare(Picture source, List<Filter> filters);
}
