package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Table(name = "posts")
@Entity
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"subscriptions"})
    private User author;
    @Column(name = "text")
    private String text;
    @OneToMany(mappedBy = "post")
    private List<Picture> pictures;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime pubDate;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likesList;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    public Post() {
        pictures = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author=" + author +
                ", text='" + text + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }
}
