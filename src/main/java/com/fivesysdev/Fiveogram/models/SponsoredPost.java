package com.fivesysdev.Fiveogram.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sponsored_posts")
public class SponsoredPost implements HasLikeNotificationRecipients {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "sponsor_id", referencedColumnName = "id")
    private User sponsor;

    @Override
    public List<User> getLikeNotificationRecipients() {
        return Arrays.asList(post.getAuthor(), getSponsor());
    }
}
