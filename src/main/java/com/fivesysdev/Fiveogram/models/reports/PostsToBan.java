package com.fivesysdev.Fiveogram.models.reports;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts_to_ban")
public class PostsToBan extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    PostReport postReport;
}
