package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
@Entity
public class Post implements HasLikeNotificationRecipients {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")

    private User author;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Picture picture;
    @Column(name = "created_at")
    private LocalDate pubDate;
    @OneToMany(mappedBy = "post")
    private List<Like> likesList;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;

    @Override
    public List<User> getLikeNotificationRecipients() {
        return Collections.singletonList(this.author);
    }

    public void addLike(Like like) {
        likesList.add(like);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}
