package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "created_at")
    private LocalDateTime created;
    @Column(name = "path")
    private String path;
    @Column(name = "token")
    private String token;

    @OneToMany(mappedBy = "picture")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "avatar")
    @JsonIgnore
    private List<User> users;
}
