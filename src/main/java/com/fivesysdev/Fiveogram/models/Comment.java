package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"name","surname","password","role","subscriptions"})
    private User author;
    @Column(name = "text")
    private String text;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime published;
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"author","text","pubDate","likesList","commentList"})
    private Post post;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<CommentLike> commentLikes;

}
