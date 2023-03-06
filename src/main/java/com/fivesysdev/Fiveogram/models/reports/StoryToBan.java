package com.fivesysdev.Fiveogram.models.reports;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stories_to_ban")
public class StoryToBan extends BaseEntity {
    @OneToOne
    StoryReport storyReport;
}
