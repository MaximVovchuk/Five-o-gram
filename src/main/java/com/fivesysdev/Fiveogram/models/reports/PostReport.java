package com.fivesysdev.Fiveogram.models.reports;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import com.fivesysdev.Fiveogram.models.Post;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_reports")
@Entity
public class PostReport extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @Column(name = "text")
    private String text;
}
