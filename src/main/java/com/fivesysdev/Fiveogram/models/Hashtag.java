package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hashtag")
@NoArgsConstructor
@Getter
@Setter
public class Hashtag extends BaseEntity{
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    public Hashtag(String content, Post post) {
        this.content = content;
        this.post = post;
    }
}

