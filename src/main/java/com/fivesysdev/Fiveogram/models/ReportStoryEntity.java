package com.fivesysdev.Fiveogram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "story_reports")
@Entity
public class ReportStoryEntity extends BaseEntity{
    @ManyToOne()
    @JoinColumn(name = "story_id", referencedColumnName = "id")
    private Story story;
    @Column(name = "text")
    private String text;
}
