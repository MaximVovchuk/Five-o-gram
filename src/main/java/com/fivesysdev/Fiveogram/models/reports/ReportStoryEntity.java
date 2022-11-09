package com.fivesysdev.Fiveogram.models.reports;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import com.fivesysdev.Fiveogram.models.Story;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "story_reports")
@Entity
public class ReportStoryEntity extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "story_id", referencedColumnName = "id")
    private Story story;
    @Column(name = "text")
    private String text;

}
