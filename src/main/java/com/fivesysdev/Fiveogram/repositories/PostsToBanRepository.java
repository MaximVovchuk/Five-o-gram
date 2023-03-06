package com.fivesysdev.Fiveogram.repositories;

import com.fivesysdev.Fiveogram.models.reports.PostsToBan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsToBanRepository extends JpaRepository<PostsToBan, Long> {
    void deleteByPostReport_Id(Long postReport_id);
}
