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
@Table(name = "post_reports")
@Entity
public class ReportPostEntity extends BaseEntity{
    @ManyToOne()
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @Column(name = "text")
    private String text;
}
