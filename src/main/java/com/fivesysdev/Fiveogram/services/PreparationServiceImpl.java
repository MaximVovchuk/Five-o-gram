package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Filter;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.serviceInterfaces.PreparationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PreparationServiceImpl implements PreparationService {
    public Picture prepare(Picture source, List<Filter> filters) {
        Picture prepared = source;
        for (Filter filter : filters) {
            prepared = filter.apply(prepared);
        }
        return prepared;
    }
}
