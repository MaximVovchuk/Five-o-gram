package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pictures")
@Entity
public class Picture extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @JsonIgnore
    private Post post;
    @Column(name = "path")
    private String path;
    @ToString.Exclude
    @OneToMany(mappedBy = "picture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mark> marks;
}