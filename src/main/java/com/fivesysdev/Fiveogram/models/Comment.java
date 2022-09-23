package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
//    @JsonIgnoreProperties({"friendships"})
    private User author;
    @Column(name = "text")
    private String text;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime published;
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @JsonIgnore
    private Post post;
}
