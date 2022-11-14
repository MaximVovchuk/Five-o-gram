package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "marks")
public class Mark extends BaseEntity {
    @Column(name = "width")
    private int width;
    @Column(name = "height")
    private int height;
    @Column(name = "username")
    private String username;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Picture picture;
}