package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"author","text","pubDate","likesList","commentList"})
    private Post post;

    @ManyToOne
    @JoinColumn(name = "who_likes_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"name","surname","password","role","subscriptions"})
    private User whoLikes;

    public Like(Post post, User whoLikes) {
        this.post = post;
        this.whoLikes = whoLikes;
    }

}
