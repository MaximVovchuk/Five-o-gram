package com.fivesysdev.Fiveogram.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Picture picture;
}